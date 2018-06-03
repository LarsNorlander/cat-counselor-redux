package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item
import Score
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Test

class CounselorTest {

    private val anyStrand = "any strand"
    private val anyCriterion: Criterion = "any criterion"

    @Test
    fun `get statistics for strand with 1 match and 1 miss for 1 criterion`() {
        val strand = Strand(name = anyStrand, requirements = mapOf(anyCriterion to setOf("A", "B")))
        val specification = listOf(strand)
        val records: Map<Criterion, Map<Item, Score>> = mapOf(anyCriterion to mapOf("A" to 1.0, "B" to 0.0))

        val result: Map<Criterion, Statistics> = Counselor(specification).computeStatistics(
                strandName = anyStrand,
                records = records)

        assertThat(result[anyCriterion]!!.matches, `is`(setOf("A")))
        assertThat(result[anyCriterion]!!.misses, `is`(setOf("B")))
    }

    @Test
    fun `average grade is included in strengths`() {
        val strand = Strand(name = anyStrand, requirements = mapOf(anyCriterion to setOf("A", "B")))
        val specification = listOf(strand)
        val records: Map<Criterion, Map<Item, Score>> = mapOf(anyCriterion to mapOf("A" to 1.0, "B" to 1.0))

        val result: Map<Criterion, Statistics> = Counselor(specification).computeStatistics(
                strandName = anyStrand,
                records = records)

        assertThat(result[anyCriterion]!!.matches, `is`(setOf("A", "B")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws exception for non-existent strand`() {
        Counselor(emptyList()).computeStatistics(anyStrand, emptyMap())
    }

    @Test
    fun `strand with more matches scores higher`() {
        val stem = Strand(name = "STEM", requirements = mapOf(anyCriterion to setOf("A", "B")))
        val abm = Strand(name = "ABM", requirements = mapOf(anyCriterion to setOf("A", "C")))
        val specification = listOf(stem, abm)
        val scores: Map<Item, Score> = mapOf("A" to 1.0, "B" to 1.0, "C" to 0.0)

        val strandScores: Map<String, Score> = Counselor(specification).scoreStrands(
                criterion = anyCriterion,
                scores = scores)

        assertThat(strandScores["STEM"]!!, `is`(greaterThan(strandScores["ABM"]!!)))
    }

    @Test
    fun `strand with equal matches but less misses scores higher`() {
        val stem = Strand(name = "STEM", requirements = mapOf(anyCriterion to setOf("A", "B", "C")))
        val abm = Strand(name = "ABM", requirements = mapOf(anyCriterion to setOf("A", "B")))
        val specification = listOf(stem, abm)
        val scores: Map<Item, Score> = mapOf("A" to 1.0, "B" to 1.0, "C" to 0.0)

        val strandScores: Map<String, Score> = Counselor(specification).scoreStrands(
                criterion = anyCriterion,
                scores = scores)

        assertThat(strandScores["ABM"]!!, `is`(greaterThan(strandScores["STEM"]!!)))
    }

    @Test
    fun `strands with equal matches and misses score the same`() {
        val stem = Strand(name = "STEM", requirements = mapOf(anyCriterion to setOf("A", "B", "C")))
        val abm = Strand(name = "ABM", requirements = mapOf(anyCriterion to setOf("A", "B", "C")))
        val specification = listOf(stem, abm)
        val scores: Map<Item, Score> = mapOf("A" to 1.0, "B" to 1.0, "C" to 0.0)

        val strandScores: Map<String, Score> = Counselor(specification).scoreStrands(
                criterion = anyCriterion,
                scores = scores)

        assertThat(strandScores["ABM"]!!, `is`(equalTo(strandScores["STEM"]!!)))
    }
}


