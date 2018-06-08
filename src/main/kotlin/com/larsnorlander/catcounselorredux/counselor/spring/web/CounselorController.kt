package com.larsnorlander.catcounselorredux.counselor.spring.web

import com.larsnorlander.catcounselorredux.counselor.Counselor
import com.larsnorlander.catcounselorredux.counselor.Specification
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class CounselorController(
    val specification: Specification,
    val counselor: Counselor
) {

    @GetMapping("/form")
    fun home(model: Model): String {
        model.addAttribute("spec", specification.items)
        return "form"
    }

}