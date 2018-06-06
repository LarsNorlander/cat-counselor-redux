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

        val items: Map<Criterion, Item> = specification.items

        assertThat(items.keys, `is`(equalTo(setOf("Grades", "NCAE"))))
    }
}