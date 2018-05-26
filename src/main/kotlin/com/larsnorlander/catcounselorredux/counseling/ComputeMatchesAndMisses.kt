package com.larsnorlander.catcounselorredux.counseling

fun Set<String>.computeMatchesAndMissesFor(requirements: Set<String>): MatchesAndMissesTuple {
    val matches = this.intersect(requirements)
    val misses = requirements.minus(this)
    return MatchesAndMissesTuple(matches, misses)
}