package org.softuni.finalproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SummaryController {

    @GetMapping("/summary")
    public String summary() {
        return "summary";
    }
}
