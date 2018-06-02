package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item
import Score
import java.util.stream.DoubleStream

class Counselor(private val specification: List<Strand>) {
    fun computeStatistics(strandName: String, records: Map<Criterion, Map<Item, Score>>): Map<Criterion, Statistics> {
        val strand = specification.find { it.name == strandName }!!
        val statistics = mutableMapOf<Criterion, Statistics>()
        for ((criterion, scores) in records) {
            if (strand doesNotHaveRequirementsFor criterion) continue
            val strengths = computeStrengths(scores)
            statistics[criterion] = strand.statistics(criterion, strengths)
        }
        return statistics
    }

    private infix fun Strand.doesNotHaveRequirementsFor(criterion: Criterion) =
            !this.requirements.containsKey(criterion)

    private fun Strand.statistics(criterion: Criterion, strengths: Set<Item>): Statistics {
        return Statistics(
                matches = strengths.intersect(this.requirements[criterion]!!),
                misses = this.requirements[criterion]!!.minus(strengths))
    }

    private fun computeStrengths(scores: Map<Item, Score>): Set<Item> {
        val averageScore = DoubleStream.of(*scores.values.toDoubleArray()).sum() / scores.values.size
        return scores.filter { it.value >= averageScore }.keys
    }
}

data class Strand(val name: String, val requirements: Map<Criterion, Set<Item>>)

data class Statistics(val matches: Set<Item>, val misses: Set<Item>)
