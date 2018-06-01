package com.larsnorlander.catcounselorredux.counseling

import org.junit.Assert.assertEquals
import org.junit.Test

private const val SCIENCE = "Science"
private const val ENGLISH = "English"
private const val MATH = "Math"

private const val PERFECT_GRADE = 100.0
private const val MID_GRADE = 85.0
private const val FAILED_GRADE = 70.0
private const val ANY_GRADE = 0.0

class ExtractStrengthsTest {
    @Test
    fun `get only item`() {
        val data = mapOf(SCIENCE to ANY_GRADE)

        val result = data.strengths

        assertEquals(result, setOf(SCIENCE))
    }

    @Test
    fun `get obvious strength`() {
        val data = mapOf(SCIENCE to PERFECT_GRADE,
                MATH to FAILED_GRADE)

        val result = data.strengths

        assertEquals(result, setOf(SCIENCE))
    }

    @Test
    fun `get mid and perfect subjects`() {
        val data = mapOf(SCIENCE to PERFECT_GRADE,
                MATH to MID_GRADE,
                ENGLISH to FAILED_GRADE)

        val result = data.strengths

        assertEquals(result, setOf(SCIENCE, MATH))
    }

}