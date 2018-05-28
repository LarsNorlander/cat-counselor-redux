package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.Test

private const val SCIENCE = "Science"
private const val MATH = "Math"
private const val HISTORY = "History"
private const val COMPUTER = "Computer"

class ComputeMatchesAndMissesTest {

    @Test
    fun `perfect match`() {
        val subjects = setOf(SCIENCE, MATH)

        val result = subjects.computeMatchesAndMissesFor(subjects)

        assertThat(result.matches, `is`(equalTo(subjects)))
        assertThat(result.misses, `is`(empty()))
    }

    @Test
    fun `no match`() {
        val strengths = emptySet<String>()
        val requirements = setOf(SCIENCE, MATH)

        val result = strengths.computeMatchesAndMissesFor(requirements)

        assertThat(result.misses, `is`(equalTo(requirements)))
        assertThat(result.matches, `is`(empty()))
    }

    @Test
    fun `one match out of three requirements`() {
        val strengths = setOf(SCIENCE, HISTORY)
        val requirements = setOf(SCIENCE, MATH, COMPUTER)

        val result = strengths.computeMatchesAndMissesFor(requirements)

        assertThat(result.matches, `is`(equalTo(setOf(SCIENCE))))
        assertThat(result.misses, `is`(equalTo(setOf(MATH, COMPUTER))))
    }
}

