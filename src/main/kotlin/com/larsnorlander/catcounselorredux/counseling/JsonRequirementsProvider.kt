package com.larsnorlander.catcounselorredux.counseling

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream

class JsonRequirementsProvider(inputStream: InputStream) : RequirementsProvider {

    private val requirements: JsonRequirements = ObjectMapper()
            .registerKotlinModule()
            .readValue(inputStream)

    override fun getAllCriteria(): Set<String> {
        return requirements.criteria
    }

    override fun getAllStrands(): Set<String> {
        return requirements.strands
    }

    override fun getRequirementsForStrandInCriteria(strand: String, criteria: String): Set<String> {
        return requirements.data[criteria]!![strand]!!
    }
}

private data class JsonRequirements(val criteria: Set<String>,
                                    val strands: Set<String>,
                                    val data: Map<String, Map<String, Set<String>>>)