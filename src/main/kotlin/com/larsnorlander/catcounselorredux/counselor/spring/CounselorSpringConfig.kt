package com.larsnorlander.catcounselorredux.counselor.spring

import com.larsnorlander.catcounselorredux.counselor.Counselor
import com.larsnorlander.catcounselorredux.counselor.Specification
import com.larsnorlander.catcounselorredux.counselor.Strand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CounselorSpringConfig {
    
    @Bean
    fun counselor(specification: Specification): Counselor {
        return Counselor(specification)
    }

    @Bean
    fun specification(): Specification {
        return Specification(listOf(
                Strand(name = "STEM", requirements = mapOf(
                        "Grades" to setOf(
                                "Science",
                                "Math",
                                "Computer"
                        ),
                        "NCAE" to setOf(
                                "Scientific Ability",
                                "Mathematical Ability",
                                "Logical Reasoning Ability",
                                "Visual Manipulative Ability"
                        )
                )),
                Strand(name = "ABM", requirements = mapOf(
                        "Grades" to setOf(
                                "English",
                                "Math",
                                "Computer"
                        ),
                        "NCAE" to setOf(
                                "Mathematical Ability",
                                "Reading Comprehension",
                                "Verbal Ability"
                        )
                )),
                Strand(name = "HUMSS", requirements = mapOf(
                        "Grades" to setOf(
                                "English",
                                "Values Education",
                                "Social Studies"
                        ),
                        "NCAE" to setOf(
                                "Reading Comprehension",
                                "Non Verbal Ability",
                                "Verbal Ability"
                        )
                ))
        ))
    }

}