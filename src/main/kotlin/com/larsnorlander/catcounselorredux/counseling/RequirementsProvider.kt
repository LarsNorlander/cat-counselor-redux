package com.larsnorlander.catcounselorredux.counseling

interface RequirementsProvider {

    fun getAllCriteria(): Set<String>

    fun getAllStrands(): Set<String>

    fun getRequirementsFor(strand: String, criteria: String): Set<String>

}