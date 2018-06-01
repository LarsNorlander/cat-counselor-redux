package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Test

class CounselorTest {

    @Test
    fun `item with more matches is higher`() {
        val counselor = Counselor(MockRequirementsProvider(
                criteriaData= setOf("Grades"),
                strandsData = setOf("ABM", "STEM"),
                requirements = { strand, _ ->
                    when (strand) {
                        "STEM" -> setOf("A", "B", "C")
                        "ABM" -> setOf("A", "B", "D")
                        else -> throw RuntimeException("Non-existent strand")
                    }
                }
        ))
        val records: Map<Criterion, Records> = mapOf("Grades" to mapOf(
                "A" to 100.0,
                "B" to 100.0,
                "C" to 100.0,
                "D" to 70.0
        ))
        val preferences = listOf("STEM", "ABM")

        val result = counselor.assess(records = records, preferences = preferences)

        assertThat(result.ranking["STEM"]!!, `is`(greaterThan(result.ranking["ABM"]!!)))
    }

    // Test tie matches and item with less requirements is higher
    @Test
    fun `matches tie and item with less requirements is higher`() {
        val counselor = Counselor(MockRequirementsProvider(
                criteriaData = setOf("Grades"),
                strandsData = setOf("STEM", "ABM"),
                requirements = { strand, _ ->
                    when (strand) {
                        "STEM" -> setOf("A", "B", "C", "D")
                        "ABM" -> setOf("A", "B", "C")
                        else -> throw RuntimeException("Non-existent strand")
                    }
                }
        ))
        val records: Map<Criterion, Records> = mapOf("Grades" to mapOf(
                "A" to 100.0,
                "B" to 100.0,
                "C" to 100.0,
                "D" to 70.0
        ))
        val preferences = listOf("STEM", "ABM")

        val result = counselor.assess(records = records, preferences = preferences)

        assertThat(result.ranking["ABM"]!!, `is`(greaterThan(result.ranking["STEM"]!!)))
    }

    private class MockRequirementsProvider(
            val criteriaData: Set<Criterion>,
            val strandsData: Set<Strand>,
            val requirements: (String, String) -> Set<String>
    ) : RequirementsProvider {

        override val criteria: Set<Criterion>
            get() = criteriaData

        override val strands: Set<Strand>
            get() = strandsData

        override fun getRequirementsFor(strand: String, criteria: String) = requirements.invoke(strand, criteria)

    }

}
