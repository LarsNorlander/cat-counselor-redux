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

            val rankedMap = requirementsProvider.getAllStrands().groupBy { strand ->
                Pair(matchesAndMisses[strand]!!.matches.size, matchesAndMisses[strand]!!.misses.size)
            }.toSortedMap(compareByDescending<Pair<Int, Int>> { it.first }.thenBy { it.second })

            var allocatableScore = requirementsProvider.getAllStrands().size
            val resultMap = mutableMapOf<String, Double>()

            for (strands in rankedMap.values) {
                var scoreForTier = 0
                for (strand in strands) {
                    scoreForTier += allocatableScore
                    allocatableScore--
                }
                for (strand in strands) {
                    resultMap[strand] = scoreForTier / strands.size.toDouble()
                }
            }

            return CounselorResult(resultMap.toMap())
        }
        TODO("Doesn't actually loop around multiple criteria yet.")
    }

}

data class CounselorResult(val ranking: Map<String, Double>)