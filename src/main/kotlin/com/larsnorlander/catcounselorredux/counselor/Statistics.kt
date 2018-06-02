package com.larsnorlander.catcounselorredux.counselor

import Item

data class Statistics(val matches: Set<Item>, val misses: Set<Item>)