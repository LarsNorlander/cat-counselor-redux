package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item

data class Specification(val strands: List<Strand>) {
    val items: Map<Criterion, Item>
        get() {
            TODO()
        }
}