package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item

data class Strand(val name: String, val requirements: Map<Criterion, Set<Item>>)