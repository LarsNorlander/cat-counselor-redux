package com.larsnorlander.catcounselorredux.counseling

typealias Criterion = String
typealias Records = Map<Item, Score>
typealias Item = String
typealias Score = Int
typealias Strand = String
typealias Strength = String

class Counselor(private val requirementsProvider: RequirementsProvider) {

    fun assess(records: Map<Criterion, Records>, preferences: List<Strand>): CounselorResult {
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

    private fun Set<Strength>.computeMatchesAndMissesFor(strand: Strand, criterion: Criterion): MatchesAndMissesPair {
        val requirements = requirementsProvider.getRequirementsFor(criteria = criterion, strand = strand)
        val matches = this.intersect(requirements)
        val misses = requirements.minus(this)
        return MatchesAndMissesPair(matches, misses)
    }

}

data class CounselorResult(val ranking: Map<String, Double>)