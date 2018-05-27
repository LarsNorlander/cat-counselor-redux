package com.larsnorlander.catcounselorredux.counseling

import org.junit.Before

class JsonRequirementsProviderTestImpl : RequirementsProviderTest() {
    @Before
    fun setUp() {
        val data = "{\n" +
                "  \"criteria\": [\"Grades\", \"Awards\"],\n" +
                "  \"strands\": [\"STEM\", \"ABM\"],\n" +
                "  \"data\": {\n" +
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
        requirementsProvider = JsonRequirementsProvider(data.byteInputStream())
    }
}