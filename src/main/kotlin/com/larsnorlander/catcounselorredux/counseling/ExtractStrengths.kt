package com.larsnorlander.catcounselorredux.counseling

import java.util.stream.IntStream

fun Map<String, Int>.extractStrengths(): Set<String> {
    val average: Int = this.computeAverageValue()
    return this.extractKeysWithValueGreaterOrEqual(average)
}

private fun Map<String, Int>.extractKeysWithValueGreaterOrEqual(average: Int) =
        this.filter { it.value >= average }.keys

private fun Map<String, Int>.computeAverageValue() =
        IntStream.of(*this.values.toIntArray()).sum() / this.size
