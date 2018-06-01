package com.larsnorlander.catcounselorredux.counseling

import org.junit.Before

class JsonSpecificationTestImpl : SpecificationTest() {
    @Before
    fun setUp() {
        val data = "{\n" +
                "  \"criteria\": [\"Grades\", \"Awards\"],\n" +
                "  \"strands\": [\"STEM\", \"ABM\"],\n" +
                "  \"requirements\": {\n" +
                "    \"Grades\": {\n" +
                "      \"STEM\": [\"Science\", \"Math\"],\n" +
                "      \"ABM\":[\"English\"]\n" +
                "    },\n" +
                "    \"Awards\": {\n" +
                "      \"STEM\": [\"Science\", \"Math\"],\n" +
                "      \"ABM\":[\"English\"]\n" +
                "    }  \n" +
                "  }\n" +
                "}"
        specification = JsonSpecification(data.byteInputStream())
    }
}