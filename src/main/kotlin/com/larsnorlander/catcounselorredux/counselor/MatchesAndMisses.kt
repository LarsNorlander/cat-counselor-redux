package com.larsnorlander.catcounselorredux.counselor

import Item

data class MatchesAndMisses(val matches: Set<Item>, val misses: Set<Item>)