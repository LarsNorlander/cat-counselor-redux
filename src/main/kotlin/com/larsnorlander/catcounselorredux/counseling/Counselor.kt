package com.larsnorlander.catcounselorredux.counseling

import java.util.*

typealias Records = Map<String, Map<String, Int>>

class Counselor(private val requirementsProvider: RequirementsProvider) {

    fun assess(records: Records, preferences: List<String>): CounselorResult {
        for (criterion in records.keys) {
            val strengths = records[criterion]!!.extractStrengths()
            val matchesAndMisses = requirementsProvider.getAllStrands().map { strand ->
                val requirements = requirementsProvider.getRequirementsFor(
                        criteria = criterion,
                        strand = strand)
                strand to strengths.computeMatchesAndMissesFor(requirements)
            }.toMap()

            // go through matches and misses, the total amount of score that could be given
            // is (n(n+1))/2. E.g. if n = 3, then total amount of score that could be given is
            // 1 + 2 + 3. If

            val matchesComparator = Comparator.comparing { strand: String -> matchesAndMisses[strand]!!.matches.size }
            val missesComparator = Comparator.comparing { strand: String -> matchesAndMisses[strand]!!.misses.size }
            val rankedList = requirementsProvider.getAllStrands()
                    .sortedWith(matchesComparator.reversed()
                                    .thenComparing(missesComparator))
            return CounselorResult(rankedList)
        }
        TODO("Doesn't actually loop around multiple criteria yet.")
    }

    private fun sumRange(range: IntRange) {
        var sum = 0
        for (i in range) {
            sum += i
        }
    }

}

data class CounselorResult(val ranking: Map<String, Double>)