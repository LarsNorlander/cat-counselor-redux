package com.larsnorlander.catcounselorredux.counseling

interface RequirementsProvider {

    fun getAllCriteria(): Set<String>

    fun getRequirementsForCriteria(criteria: String): Map<String, Set<String>>

}