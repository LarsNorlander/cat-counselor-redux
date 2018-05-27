package com.larsnorlander.catcounselorredux.counseling

fun Set<String>.computeMatchesAndMissesFor(requirements: Set<String>): MatchesAndMissesPair {
    val matches = this.intersect(requirements)
    val misses = requirements.minus(this)
    return MatchesAndMissesPair(matches, misses)
}