package com.larsnorlander.catcounselorredux.counseling

private const val NCAE = "NCAE"
private const val GRADES = "Grades"

private const val ABCD_XYZ = "ABCD_XYZ"
private const val ABE_WXY = "ABE_WXY"
private const val BCF_UVW = "BCF_UVW"

class CounselorTest {

    private val counselor: Counselor = Counselor(MockRequirementsProvider())

}

private class MockRequirementsProvider : RequirementsProvider {

    override fun getAllCriteria(): Set<String> {
        return setOf(GRADES, NCAE)
    }

    override fun getAllStrands(): Set<String> {
        return setOf(ABCD_XYZ, ABE_WXY, BCF_UVW)
    }

    override fun getRequirementsFor(strand: String, criteria: String) = when (strand) {
        ABCD_XYZ -> criteria.requirementsFor(
                grades = setOf("A", "B", "C", "D"),
                ncae = setOf("X", "Y", "Z"))
        ABE_WXY -> criteria.requirementsFor(
                grades = setOf("A", "B", "E"),
                ncae = setOf("W", "X", "Y"))
        BCF_UVW -> criteria.requirementsFor(
                grades = setOf("B", "C", "F"),
                ncae = setOf("U", "V", "W"))
        else -> throw RuntimeException("Non-existent strand")
    }

    private fun String.requirementsFor(grades: Set<String>, ncae: Set<String>) = when (this) {
        GRADES -> grades
        NCAE -> ncae
        else -> throw RuntimeException("Non-existent criteria")
    }
}