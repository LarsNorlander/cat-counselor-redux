package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

abstract class RequirementsProviderTest {

    protected lateinit var requirementsProvider: RequirementsProvider

    @Test
    fun `get all criteria`() {
        val criteria = requirementsProvider.getAllCriteria()

        assertThat(criteria, `is`(equalTo(setOf("Grades", "Awards"))))
    }

    @Test
    fun `get all strands in criteria`() {
        val gradeStrands = requirementsProvider.getRequirementsForCriteria("Grades")
                .keys

        assertThat(gradeStrands, `is`(equalTo(setOf("STEM", "ABM"))))
    }

    @Test
    fun `get all requirements from strand in criteria`() {
        val requirements = requirementsProvider.getRequirementsForCriteria("Grades")["STEM"]

        assertThat(requirements, `is`(equalTo(setOf("Science", "Math"))))
    }
}