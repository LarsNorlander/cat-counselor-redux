package com.larsnorlander.catcounselorredux.counseling

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.junit.Test

class ComputeMatchesAndMissesTest {

    @Test
    fun `perfect match`() {
        val strengths = setOf("Science", "Math")
        val requirements = setOf("Science", "Math")

        val result = strengths.computeMatchesAndMissesFor(requirements)

        assertThat(result.matches, `is`(equalTo(setOf("Science", "Math"))))
        assertThat(result.misses, `is`(empty()))
    }

    @Test
    fun `no match`() {
        val strengths = emptySet<String>()
        val requirements = setOf("Science", "Math")

        val result = strengths.computeMatchesAndMissesFor(requirements)

        assertThat(result.misses, `is`(equalTo(setOf("Science", "Math"))))
        assertThat(result.matches, `is`(empty()))
    }

    @Test
    fun `one match out of three requirements`() {
        val strengths = setOf("Science", "History")
        val requirements = setOf("Science", "Math", "Computer")

        val result = strengths.computeMatchesAndMissesFor(requirements)

        assertThat(result.matches, `is`(equalTo(setOf("Science"))))
        assertThat(result.misses, `is`(equalTo(setOf("Math", "Computer"))))
    }
}

