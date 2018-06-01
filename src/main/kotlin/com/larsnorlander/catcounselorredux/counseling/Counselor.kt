package com.larsnorlander.catcounselorredux.counseling

val descendingMatchesAndAscendingMisses = compareByDescending<Pair<Int, Int>> { it.first }.thenBy { it.second }

class Counselor(private val requirementsProvider: RequirementsProvider) {

    fun assess(data: Map<Criterion, Records>, preferences: List<Strand>): CounselorResult {
        for ((criterion, records) in data) {
            val strengths = records.strengths
            val matchesAndMisses = requirementsProvider.strands.map { strand ->
                strand to strengths.computeMatchesAndMissesFor(strand = strand, criterion = criterion)
            }.toMap()

            val rankedMap = requirementsProvider.strands.groupBy { strand ->
                matchesAndMisses[strand]!!.matchesCount to matchesAndMisses[strand]!!.missesCount
            }.toSortedMap(comparator = descendingMatchesAndAscendingMisses)

            var allocatableScore = requirementsProvider.strands.size
            val resultMap = mutableMapOf<Strand, Score>()
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

    private fun Set<Strength>.computeMatchesAndMissesFor(strand: Strand, criterion: Criterion): MatchesAndMissesPair {
        val requirements = requirementsProvider.getRequirementsFor(criteria = criterion, strand = strand)
        val matches = this.intersect(requirements)
        val misses = requirements.minus(this)
        return MatchesAndMissesPair(matches, misses)
    }

}

data class CounselorResult(val ranking: Map<String, Double>)