package com.larsnorlander.catcounselorredux.counselor

import Criterion
import Item
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SpecificationTest {
    @Test
    fun `specification lists all criteria`() {
        val specification = Specification(listOf(
                Strand(name = "STEM", requirements = mapOf(
                        "Grades" to setOf(),
                        "NCAE" to setOf()))))

        val items: Map<Criterion, Set<Item>> = specification.items

        assertThat(items.keys, `is`(equalTo(setOf("Grades", "NCAE"))))
    }

    @Test
    fun `collect all items from one criteria over several strands`() {
        val specification = Specification(listOf(
                Strand(name = "STEM", requirements = mapOf(
                        "Grades" to setOf("A", "B"),
                        "NCAE" to setOf("X")
                )),
                Strand(name = "ABM", requirements = mapOf(
                        "Grades" to setOf("B", "C"),
                        "NCAE" to setOf("Y", "Z")
                ))
        ))

        val items: Map<Criterion, Set<Item>> = specification.items

        assertThat(items["Grades"], `is`(equalTo(setOf("A", "B", "C"))))
        assertThat(items["NCAE"], `is`(equalTo(setOf("X", "Y", "Z"))))
    }
}