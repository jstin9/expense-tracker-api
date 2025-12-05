package com.jstn9.expensetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @GetMapping({"/", "/index", "/home"})
    public String index() {
        return "forward:/index.html";
    }

    @RequestMapping("/{path:^(?!api|assets|static|swagger-ui|v3|actuator|favicon\\.ico)[^\\.]*}/**")
    public String forward() {
        return "forward:/index.html";
    }
}
