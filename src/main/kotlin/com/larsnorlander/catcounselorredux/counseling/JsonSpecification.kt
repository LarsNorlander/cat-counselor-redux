package com.larsnorlander.catcounselorredux.counseling

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream

class JsonSpecification(inputStream: InputStream) : Specification {

    private val specificationModel: JsonSpecificationModel = ObjectMapper()
            .registerKotlinModule()
            .readValue(inputStream)

    override val criteria: Set<Criterion>
        get() = specificationModel.criteria

    override val strands: Set<Strand>
        get() = specificationModel.strands

    override fun requirementsFor(strand: Strand, criteria: Criterion): Set<Item> {
        return specificationModel.requirements[criteria]!![strand]!!
    }
}

private data class JsonSpecificationModel(
        val criteria: Set<Criterion>,
        val strands: Set<Strand>,
        val requirements: Map<Criterion, Map<Strand, Set<Item>>>
)