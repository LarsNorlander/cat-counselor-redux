package com.larsnorlander.catcounselorredux.counseling

data class StrandStatistics(val matches: Set<Item>, val misses: Set<Item>) {
    val matchesCount
        get() = matches.size
    val missesCount
        get() = misses.size
}

