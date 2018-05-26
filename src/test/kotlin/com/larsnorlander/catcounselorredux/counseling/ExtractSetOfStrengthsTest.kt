package com.larsnorlander.catcounselorredux.counseling

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtractSetOfStrengthsTest {

    private companion object {
        const val SCIENCE = "Science"
        const val ENGLISH = "English"
        const val MATH = "Math"

        const val PERFECT_GRADE = 100
        const val MID_GRADE = 85
        const val FAILED_GRADE = 70
        const val ANY_GRADE = 0
    }

    @Test
    fun `get only item`() {
        val data = mapOf(SCIENCE to ANY_GRADE)

        val result = data.extractSetOfStrengths()

        assertEquals(result, setOf(SCIENCE))
    }

    @Test
    fun `get obvious strength`() {
        val data = mapOf(SCIENCE to PERFECT_GRADE,
                MATH to FAILED_GRADE)

        val result = data.extractSetOfStrengths()

        assertEquals(result, setOf(SCIENCE))
    }

    @Test
    fun `get mid and perfect subjects`() {
        val data = mapOf(SCIENCE to PERFECT_GRADE,
                MATH to MID_GRADE,
                ENGLISH to FAILED_GRADE)

        val result = data.extractSetOfStrengths()

        assertEquals(result, setOf(SCIENCE, MATH))
    }

}