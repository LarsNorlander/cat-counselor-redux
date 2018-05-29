package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CounselorTest {

    @Test
    fun `item with more matches is higher`() {
        val counselor = Counselor(MockRequirementsProvider(
                criteria = setOf("Grades"),
                strands = setOf("ABM", "STEM"),
                requirements = { strand, _ ->
                    when (strand) {
                        "STEM" -> setOf("A", "B", "C")
                        "ABM" -> setOf("A", "B", "D")
                        else -> throw RuntimeException("Non-existent strand")
                    }
                }
        ))
        val records: Records = mapOf("Grades" to mapOf(
                "A" to 100,
                "B" to 100,
                "C" to 100,
                "D" to 70
        ))
        val preferences = listOf("STEM", "ABM")

        val result = counselor.assess(records = records, preferences = preferences)

        assertThat(result.ranking, `is`(equalTo(listOf("STEM", "ABM"))))
    }

    // Test tie matches and item with less requirements is higher
    @Test
    fun `matches tie and item with less requirements is higher`() {
        val counselor = Counselor(MockRequirementsProvider(
                criteria = setOf("Grades"),
                strands = setOf("STEM", "ABM"),
                requirements = { strand, _ ->
                    when (strand) {
                        "STEM" -> setOf("A", "B", "C", "D")
                        "ABM" -> setOf("A", "B", "C")
                        else -> throw RuntimeException("Non-existent strand")
                    }
                }
        ))
        val records: Records = mapOf("Grades" to mapOf(
                "A" to 100,
                "B" to 100,
                "C" to 100,
                "D" to 70
        ))
        val preferences = listOf("STEM", "ABM")

        val result = counselor.assess(records = records, preferences = preferences)

        assertThat(result.ranking, `is`(equalTo(listOf("ABM", "STEM"))))
    }

    private class MockRequirementsProvider(
            val criteria: Set<String>,
            val strands: Set<String>,
            val requirements: (String, String) -> Set<String>
    ) : RequirementsProvider {

        override fun getAllCriteria() = criteria

        override fun getAllStrands() = strands

        override fun getRequirementsFor(strand: String, criteria: String) = requirements.invoke(strand, criteria)

    }

}
