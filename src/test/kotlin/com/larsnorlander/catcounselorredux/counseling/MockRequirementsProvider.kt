package com.larsnorlander.catcounselorredux.counseling

private const val NCAE = "NCAE"
private const val GRADES = "Grades"

private const val STEM = "STEM"
private const val ABM = "ABM"
private const val HUMSS = "HUMSS"

class MockRequirementsProvider : RequirementsProvider {

    override fun getAllCriteria(): Set<String> {
        return setOf(GRADES, NCAE)
    }

    override fun getAllStrands(): Set<String> {
        return setOf(STEM, ABM, HUMSS)
    }

    override fun getRequirementsForStrandInCriteria(strand: String, criteria: String) = when (strand) {
        STEM -> criteria.requirementsFor(
                grades = setOf("Science", "Math", "Computer"),
                ncae = setOf("Logical Ability", "Scientific Thinking", "Programming"))
        ABM -> criteria.requirementsFor(
                grades = setOf("English", "TLE", "Math"),
                ncae = setOf("Logical Ability", "Communication", "Creativity", "Finance"))
        HUMSS -> criteria.requirementsFor(
                grades = setOf("English", "Social Studies", "History"),
                ncae = setOf("Communication", "Creativity", "Empathy"))
        else -> throw RuntimeException("Non-existent strand")
    }

    private fun String.requirementsFor(grades: Set<String>, ncae: Set<String>) = when (this) {
        GRADES -> grades
        NCAE -> ncae
        else -> throw RuntimeException("Non-existent criteria")
    }
}