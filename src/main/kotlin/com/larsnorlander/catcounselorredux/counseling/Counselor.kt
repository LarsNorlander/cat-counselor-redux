package com.larsnorlander.catcounselorredux.counseling

typealias Records = Map<String, Map<String, Int>>

class Counselor(private val requirementsProvider: RequirementsProvider) {

    fun assess(records: Records, preferences: List<String>): CounselorResult {
        for (criterion in records.keys) {
            val strengths = records[criterion]!!.getStrengths()
            val matchesAndMisses = requirementsProvider.getAllStrands()
                    .map { strand -> strand to strengths.computeMatchesAndMissesFor(strand = strand, criterion = criterion) }.toMap()

            val rankedMap = requirementsProvider.getAllStrands().groupBy { strand ->
                matchesAndMisses[strand]!!.matches.size to matchesAndMisses[strand]!!.misses.size
            }.toSortedMap(compareByDescending<Pair<Int, Int>> { it.first }.thenBy { it.second })

            var allocatableScore = requirementsProvider.getAllStrands().size
            val resultMap = mutableMapOf<String, Double>()

            for (strands in rankedMap.values) {
                var scoreForTier = 0
                strands.forEach {
                    scoreForTier += allocatableScore
                    allocatableScore--
                }
                strands.forEach { strand ->
                    resultMap[strand] = scoreForTier / strands.size.toDouble()
                }
            }

            return CounselorResult(resultMap.toMap())
        }
        TODO("Doesn't actually loop around multiple criteria yet.")
    }

    private fun Set<String>.computeMatchesAndMissesFor(strand: String, criterion: String): MatchesAndMissesPair {
        val requirements = requirementsProvider.getRequirementsFor(criteria = criterion, strand = strand)
        val matches = this.intersect(requirements)
        val misses = requirements.minus(this)
        return MatchesAndMissesPair(matches, misses)
    }

}

data class CounselorResult(val ranking: Map<String, Double>)