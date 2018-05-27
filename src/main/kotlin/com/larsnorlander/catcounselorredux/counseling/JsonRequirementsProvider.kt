package com.larsnorlander.catcounselorredux.counseling

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream

typealias DataStructure = Map<String, Map<String, Set<String>>>

class JsonRequirementsProvider(inputStream: InputStream) : RequirementsProvider {

    private val data: DataStructure = ObjectMapper()
            .registerKotlinModule()
            .readValue(inputStream)

    override fun getAllCriteria(): Set<String> {
        return data.keys
    }

    override fun getRequirementsForCriteria(criteria: String): Map<String, Set<String>> {
        return data[criteria]!!
    }
}