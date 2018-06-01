package com.larsnorlander.catcounselorredux.counseling

interface RequirementsProvider {

    val criteria: Set<Criterion>

    val strands: Set<Strand>

    fun getRequirementsFor(strand: Strand, criteria: Criterion): Set<Item>

}