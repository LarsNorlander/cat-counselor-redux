package com.larsnorlander.catcounselorredux.counseling

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream

class JsonRequirementsProvider(inputStream: InputStream) : RequirementsProvider {

    private val requirements: JsonRequirements = ObjectMapper()
            .registerKotlinModule()
            .readValue(inputStream)

    override val criteria: Set<Criterion>
        get() = requirements.criteria

    override val strands: Set<Strand>
        get() = requirements.strands

    override fun getRequirementsFor(strand: Strand, criteria: Criterion): Set<Item> {
        return requirements.data[criteria]!![strand]!!
    }
}

private data class JsonRequirements(val criteria: Set<Criterion>,
                                    val strands: Set<Strand>,
                                    val data: Map<Criterion, Map<Strand, Set<Item>>>)