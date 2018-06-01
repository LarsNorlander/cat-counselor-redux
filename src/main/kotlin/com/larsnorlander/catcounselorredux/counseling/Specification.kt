package com.larsnorlander.catcounselorredux.counseling

interface Specification {

    val criteria: Set<Criterion>

    val strands: Set<Strand>

    fun requirementsFor(strand: Strand, criteria: Criterion): Set<Item>

}