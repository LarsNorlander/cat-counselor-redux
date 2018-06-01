package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

private const val GRADES = "Grades"
private const val AWARDS = "Awards"

private const val STEM = "STEM"
private const val ABM = "ABM"

private const val SCIENCE = "Science"
private const val MATH = "Math"

abstract class SpecificationTest {

    protected lateinit var specification: Specification

    @Test
    fun `get all criteria`() {
        val criteria = specification.criteria

        assertThat(criteria, `is`(equalTo(setOf(GRADES, AWARDS))))
    }

    @Test
    fun `get all strands`() {
        val strands = specification.strands

        assertThat(strands, `is`(equalTo(setOf(STEM, ABM))))
    }

    @Test
    fun `get all requirements from strand in criteria`() {
        val requirements = specification.requirementsFor(strand = STEM, criteria = GRADES)

        assertThat(requirements, `is`(equalTo(setOf(SCIENCE, MATH))))
    }
}