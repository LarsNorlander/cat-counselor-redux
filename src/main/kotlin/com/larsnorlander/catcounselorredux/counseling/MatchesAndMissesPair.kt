package com.larsnorlander.catcounselorredux.counseling

data class MatchesAndMissesPair(val matches: Set<Item>, val misses: Set<Item>) {
    val matchesCount
        get() = matches.size
    val missesCount
        get() = misses.size
}

