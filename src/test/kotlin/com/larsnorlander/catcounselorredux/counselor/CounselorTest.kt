package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item
import Score
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Test

private const val STEM = "STEM"
private const val ABM = "ABM"
private const val ANY_STRAND_NAME = "any strand"

private const val ANY_CRITERION: Criterion = "any criterion"

private const val UPPER_BOUND_SCORE = 1.0
private const val LOWER_BOUND_SCORE = 0.0

class CounselorTest {

    @Test
    fun `get statistics for strand with 1 match and 1 miss for 1 criterion`() {
        val specification = listOf(
                Strand(name = ANY_STRAND_NAME, requirements = mapOf(ANY_CRITERION to setOf("A", "B"))))
        val records: Map<Criterion, Map<Item, Score>> = mapOf(
                ANY_CRITERION to mapOf(
                        "A" to UPPER_BOUND_SCORE,
                        "B" to LOWER_BOUND_SCORE))

        val result: Map<Criterion, Statistics> = Counselor(specification).computeStatistics(
                strandName = ANY_STRAND_NAME,
                records = records)

        assertThat(result[ANY_CRITERION]!!.matches, `is`(setOf("A")))
        assertThat(result[ANY_CRITERION]!!.misses, `is`(setOf("B")))
    }

    @Test
    fun `average grade is included in strengths`() {
        val specification = listOf(
                Strand(name = ANY_STRAND_NAME, requirements = mapOf(ANY_CRITERION to setOf("A", "B"))))
        val records: Map<Criterion, Map<Item, Score>> = mapOf(
                ANY_CRITERION to mapOf(
                        "A" to UPPER_BOUND_SCORE,
                        "B" to UPPER_BOUND_SCORE))

        val result: Map<Criterion, Statistics> = Counselor(specification).computeStatistics(
                strandName = ANY_STRAND_NAME,
                records = records)

        assertThat(result[ANY_CRITERION]!!.matches, `is`(setOf("A", "B")))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `throws exception for non-existent strand`() {
        Counselor(emptyList()).computeStatistics(ANY_STRAND_NAME, emptyMap())
    }

    @Test
    fun `strand with more matches scores higher`() {
        val specification = listOf(
                Strand(name = STEM, requirements = mapOf(ANY_CRITERION to setOf("A", "B"))),
                Strand(name = ABM, requirements = mapOf(ANY_CRITERION to setOf("A", "C"))))
        val scores: Map<Item, Score> = mapOf(
                "A" to UPPER_BOUND_SCORE,
                "B" to UPPER_BOUND_SCORE,
                "C" to LOWER_BOUND_SCORE)

        val strandScores: Map<String, Score> = Counselor(specification).scoreStrands(
                criterion = ANY_CRITERION,
                scores = scores)

        assertThat(strandScores[STEM]!!, `is`(greaterThan(strandScores[ABM]!!)))
    }

    @Test
    fun `strand with equal matches but less misses scores higher`() {
        val specification = listOf(
                Strand(name = STEM, requirements = mapOf(ANY_CRITERION to setOf("A", "B", "C"))),
                Strand(name = ABM, requirements = mapOf(ANY_CRITERION to setOf("A", "B"))))
        val scores: Map<Item, Score> = mapOf(
                "A" to UPPER_BOUND_SCORE,
                "B" to UPPER_BOUND_SCORE,
                "C" to LOWER_BOUND_SCORE)

        val strandScores: Map<String, Score> = Counselor(specification).scoreStrands(
                criterion = ANY_CRITERION,
                scores = scores)

        assertThat(strandScores[ABM]!!, `is`(greaterThan(strandScores[STEM]!!)))
    }

    @Test
    fun `strands with equal matches and misses score the same`() {
        val specification = listOf(
                Strand(name = STEM, requirements = mapOf(ANY_CRITERION to setOf("A", "B", "C"))),
                Strand(name = ABM, requirements = mapOf(ANY_CRITERION to setOf("A", "B", "C"))))
        val scores: Map<Item, Score> = mapOf(
                "A" to UPPER_BOUND_SCORE,
                "B" to UPPER_BOUND_SCORE,
                "C" to LOWER_BOUND_SCORE)

        val strandScores: Map<String, Score> = Counselor(specification).scoreStrands(
                criterion = ANY_CRITERION,
                scores = scores)

        assertThat(strandScores[ABM]!!, `is`(equalTo(strandScores[STEM]!!)))
    }

    @Test
    fun `add strand scores across criteria`() {
        val specification = listOf(
            Strand(name = STEM, requirements = mapOf("Grades" to setOf("A", "B"), "NCAE" to setOf("X", "Y"))),
            Strand(name = ABM, requirements = mapOf("Grades" to setOf("A", "C"), "NCAE" to setOf("X", "Z"))))
        val counselor = Counselor(specification)
        val gradeScores: Map<Item, Score> = mapOf(
                "A" to UPPER_BOUND_SCORE,
                "B" to UPPER_BOUND_SCORE,
                "C" to LOWER_BOUND_SCORE)
        val gradeStrandScores = counselor.scoreStrands(
                criterion = "Grades",
                scores = gradeScores)
        val ncaeScores: Map<Item, Score> = mapOf(
                "X" to UPPER_BOUND_SCORE,
                "Y" to UPPER_BOUND_SCORE,
                "Z" to LOWER_BOUND_SCORE)
        val ncaeStrandScores = counselor.scoreStrands(
                criterion = "NCAE",
                scores = ncaeScores)
        val records = mapOf("Grades" to gradeScores, "NCAE" to ncaeScores)

        val overallScores: Map<String, Score> = counselor.scoreStrands(records)

        assertThat(gradeStrandScores[STEM]!! + ncaeStrandScores[STEM]!!, `is`(equalTo(overallScores[STEM])))
    }
    
}


