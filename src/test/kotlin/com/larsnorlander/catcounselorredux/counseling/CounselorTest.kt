package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Test

class CounselorTest {

    private val counselor = Counselor(MockSpecification(
            criteriaData= setOf("Grades"),
            strandsData = setOf("ABM", "STEM", "HUMSS"),
            requirements = { strand, _ ->
                when (strand) {
                    "STEM" -> setOf("A", "B", "C", "D")
                    "ABM" -> setOf("A", "B", "E")
                    "HUMSS" -> setOf("A", "B", "F")
                    else -> throw RuntimeException("Non-existent strand")
                }
            }
    ))

    @Test
    fun `item with more matches has higher score`() {
        val records: Map<Criterion, Records> = mapOf("Grades" to mapOf(
                "A" to 100.0,
                "B" to 100.0,
                "C" to 100.0,
                "D" to 70.0
        ))

        val result = counselor.assess(data = records, preferences = emptyList())

        assertThat(result.scores["STEM"]!!, `is`(greaterThan(result.scores["ABM"]!!)))
    }

    @Test
    fun `matches tie and item with less requirements is higher`() {
        val records: Map<Criterion, Records> = mapOf("Grades" to mapOf(
                "A" to 100.0,
                "B" to 100.0,
                "C" to 70.0,
                "D" to 70.0,
                "E" to 70.0
        ))

        val result = counselor.assess(data = records, preferences = emptyList())

        assertThat(result.scores["ABM"]!!, `is`(greaterThan(result.scores["STEM"]!!)))
    }

    @Test
    fun `same score for tie matches and misses`() {
        val records: Map<Criterion, Records> = mapOf("Grades" to mapOf(
                "A" to 100.0,
                "B" to 100.0,
                "E" to 70.0,
                "F" to 70.0
        ))

        val result = counselor.assess(data = records, preferences = emptyList())

        assertThat(result.scores["ABM"]!!, `is`(equalTo(result.scores["HUMSS"]!!)))
    }

    private class MockSpecification(
            val criteriaData: Set<Criterion>,
            val strandsData: Set<Strand>,
            val requirements: (String, String) -> Set<String>
    ) : Specification {

        override val criteria: Set<Criterion>
            get() = criteriaData

        override val strands: Set<Strand>
            get() = strandsData

        override fun requirementsFor(strand: String, criteria: String) = requirements.invoke(strand, criteria)

    }

}
