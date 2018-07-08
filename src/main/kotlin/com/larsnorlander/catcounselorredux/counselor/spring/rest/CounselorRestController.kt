package com.larsnorlander.catcounselorredux.counselor.spring.rest

import Criterion
import Item
import Score
import com.larsnorlander.catcounselorredux.counselor.Counselor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/counselor")
class CounselorRestController(private val counselor: Counselor) {

    @RequestMapping("{strandName}")
    fun computeStatistics(
            @PathVariable strandName: String,
            @RequestBody records: Map<Criterion, Map<Item, Score>>) =
            ResponseEntity.ok(counselor.computeStatistics(strandName = strandName, records = records))

}