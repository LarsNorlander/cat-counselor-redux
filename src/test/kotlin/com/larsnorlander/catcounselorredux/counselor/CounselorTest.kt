package com.larsnorlander.catcounselorredux.counselor

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CounselorTest {

    private val anyStrand = "any strand"
    private val anyCriterion = "any criterion"

    @Test
    fun `get statistics for strand with 1 match and 1 miss`() {
        val stem = Strand(name = anyStrand, requirements = mapOf(anyCriterion to setOf("A", "B")))
        val specification = listOf(stem)
        val records: Map<Criterion, Map<Item, Score>> = mapOf(anyCriterion to mapOf("A" to 1.0, "B" to 0.0))

        val result: Map<Criterion, Statistics> = Counselor(specification).computeStatistics(
                strandName = anyStrand,
                records = records)

        assertThat(result[anyCriterion]!!.matches.size, `is`(equalTo(1)))
        assertThat(result[anyCriterion]!!.misses.size, `is`(equalTo(1)))
    }

}


