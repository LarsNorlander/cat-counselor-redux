package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item

data class Specification(val strands: List<Strand>) {
    val items: Map<Criterion, Set<Item>>
        get() {
            val result = mutableMapOf<Criterion, MutableSet<Item>>()
            for (strand in strands) {
                for (criterion in strand.requirements.keys) {
                    if (result[criterion] != null)
                        result[criterion]!!.addAll(strand.requirements[criterion]!!)
                    else {
                        result[criterion] = mutableSetOf()
                        result[criterion]!!.addAll(strand.requirements[criterion]!!)
                    }
                }
            }
            return result
        }
}