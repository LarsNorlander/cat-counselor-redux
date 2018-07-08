package com.larsnorlander.catcounselorredux.counselor.spring.rest

import Criterion
import Item
import com.larsnorlander.catcounselorredux.counselor.Specification
import com.larsnorlander.catcounselorredux.counselor.Strand
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/specification")
class SpecificationRestController(val specification: Specification) {

    @GetMapping
    fun getStrands(): ResponseEntity<List<Strand>> {
        return ResponseEntity.ok(specification.strands)
    }

    @GetMapping("items")
    fun getSpecification(): ResponseEntity<Map<Criterion, Set<Item>>> {
        return ResponseEntity.ok(specification.items)
    }

}