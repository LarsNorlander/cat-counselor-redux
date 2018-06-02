package com.larsnorlander.catcounselorredux.counseling

import java.util.*

val descendingMatchesAndAscendingMisses = compareByDescending<Pair<Int, Int>> { it.first }
        .thenBy { it.second }

class Counselor(private val specification: Specification) {

    fun assess(data: Map<Criterion, Records>, preferences: List<Strand>): CounselorResult {
        for ((criterion, records) in data) {
            val strandStatistics = computeStrandStatistics(criterion, records)
            val rankedStrands = rankStrands(strandStatistics)
            val strandScores = scoreStrands(rankedStrands)

            return CounselorResult(strandScores)
        }
        TODO("Doesn't actually loop around multiple criteria yet.")
    }

    private fun computeStrandStatistics(criterion: Criterion, records: Records): Map<Strand, StrandStatistics> {
        val strengths = records.computeStrengths()
        return specification.strands.map { strand ->
            val strandStatistics = strengths.computeStrandStatisticsFor(strand = strand, criterion = criterion)
            strand to strandStatistics
        }.toMap()
    }

    private fun Set<Strength>.computeStrandStatisticsFor(strand: Strand, criterion: Criterion): StrandStatistics {
        val requirements = specification.requirementsFor(criteria = criterion, strand = strand)
        val matches = this.intersect(requirements)
        val misses = requirements.minus(this)
        return StrandStatistics(matches, misses)
    }

    private fun rankStrands(strandStatistics: Map<Strand, StrandStatistics>): SortedMap<Pair<Int, Int>, List<Strand>> {
        return specification.strands.groupBy { strand ->
            strandStatistics[strand]!!.matchesCount to strandStatistics[strand]!!.missesCount
        }.toSortedMap(comparator = descendingMatchesAndAscendingMisses)
    }

    private fun scoreStrands(rankedStrands: SortedMap<Pair<Int, Int>, List<Strand>>): Map<Strand, Score> {
        val result = mutableMapOf<Strand, Score>()
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

}

data class CounselorResult(val scores: Map<Strand, Score>)