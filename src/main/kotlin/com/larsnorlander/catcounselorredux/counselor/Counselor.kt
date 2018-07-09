package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item
import Score
import java.util.*
import java.util.stream.DoubleStream

class Counselor(private val specification: Specification) {
    fun computeStatistics(strandName: String, records: Map<Criterion, Map<Item, Score>>): Map<Criterion, MatchesAndMisses> {
        val strand = findStrand(strandName)
        val matchesAndMisses = mutableMapOf<Criterion, MatchesAndMisses>()
        for ((criterion, scores) in records) {
            if (strand doesNotHaveRequirementsFor criterion) continue
            val strengths = computeStrengths(scores)
            matchesAndMisses[criterion] = strand.matchesAndMisses(criterion, strengths)
        }
        return matchesAndMisses
    }

    private fun findStrand(strandName: String) = specification.strands.find { it.name == strandName }
            ?: throw IllegalArgumentException("'$strandName' isn't a valid strand.")

    private infix fun Strand.doesNotHaveRequirementsFor(criterion: Criterion) =
            !this.requirements.containsKey(criterion)

    private fun Strand.matchesAndMisses(criterion: Criterion, strengths: Set<Item>): MatchesAndMisses {
        return MatchesAndMisses(
                matches = strengths.intersect(this.requirements[criterion]!!),
                misses = this.requirements[criterion]!!.minus(strengths))
    }

    private fun computeStrengths(scores: Map<Item, Score>): Set<Item> {
        val averageScore = DoubleStream.of(*scores.values.toDoubleArray()).sum() / scores.values.size
        return scores.filter { it.value >= averageScore }.keys
    }

    fun scoreStrands(criterion: Criterion, scores: Map<Item, Score>): Map<String, Score> {
        val strengths = computeStrengths(scores)
        val matchesAndMisses = mutableMapOf<String, MatchesAndMisses>()
        for (strand in specification.strands) {
            if (!strand.requirements.containsKey(criterion)) continue
            matchesAndMisses[strand.name] = strand.matchesAndMisses(criterion, strengths)
        }
        val groupedStrands = groupStrandsByMatchesAndMisses(matchesAndMisses)
        return assignScores(groupedStrands)
    }

    private val descendingMatchesAndAscendingMisses = compareByDescending<Pair<Int, Int>> { it.first }
            .thenBy { it.second }

    private fun groupStrandsByMatchesAndMisses(strandStatistics: Map<String, MatchesAndMisses>): SortedMap<Pair<Int, Int>, List<String>> {
        return specification.strands.map { it.name }.groupBy { strand ->
            strandStatistics[strand]!!.matches.size to strandStatistics[strand]!!.misses.size
        }.toSortedMap(comparator = descendingMatchesAndAscendingMisses)
    }

    private fun assignScores(rankedStrands: SortedMap<Pair<Int, Int>, List<String>>): Map<String, Score> {
        val result = mutableMapOf<String, Score>()
        var allocatableScore = specification.strands.size
        for (strands in rankedStrands.values) {
            var scoreForTier = 0
            strands.forEach {
                scoreForTier += allocatableScore
                allocatableScore--
            }
            strands.forEach { strand ->
                result[strand] = scoreForTier / strands.size.toDouble()
            }
        }
        return result.toMap()
    }

    fun scoreStrands(records: Map<Criterion, Map<Item, Score>>): Map<String, Score> {
        return records.map { scoreStrands(it.key, it.value) }
                .reduce(::reduceStrandScores)
    }

    private fun reduceStrandScores(a: Map<String, Score>, b: Map<String, Score>): Map<String, Score> {
        val resultMap = mutableMapOf<String, Score>()
        a.keys.forEach { resultMap[it] = a[it]!! + b[it]!! }
        return resultMap
    }
}
