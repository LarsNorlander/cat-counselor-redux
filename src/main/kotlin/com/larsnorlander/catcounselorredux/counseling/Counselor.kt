package com.larsnorlander.catcounselorredux.counseling

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

            val rankedList = requirementsProvider.getAllStrands().sortedWith(
                    Comparator { o1, o2 ->
                        if (matchesAndMisses[o1]!!.matches.size == matchesAndMisses[o2]!!.matches.size )
                            matchesAndMisses[o1]!!.misses.size.compareTo(matchesAndMisses[o2]!!.misses.size)
                        else
                            matchesAndMisses[o2]!!.matches.size.compareTo(matchesAndMisses[o1]!!.matches.size)
                    }
            )
            return CounselorResult(rankedList)
        }
        TODO("Doesn't actually loop around multiple criteria yet.")
    }

}

data class CounselorResult(val ranking: List<String>)