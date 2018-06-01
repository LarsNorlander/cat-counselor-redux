package com.larsnorlander.catcounselorredux.counseling

import java.util.stream.DoubleStream

val Records.strengths
    get(): Set<Strength> {
        val average: Double = this.computeAverageScore()
        return this.extractItemsWithScoreGreaterOrEqual(average)
    }

private fun Records.extractItemsWithScoreGreaterOrEqual(average: Double) =
        this.filter { it.value >= average }.keys

private fun Records.computeAverageScore() =
        DoubleStream.of(*this.values.toDoubleArray()).sum() / this.size
