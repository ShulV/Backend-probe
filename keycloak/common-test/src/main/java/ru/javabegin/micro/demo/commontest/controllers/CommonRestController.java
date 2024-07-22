package ru.javabegin.micro.demo.commontest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class CommonRestController {

    @GetMapping("/anonymous")
    public String testAnonymous() {
        return "test anonymous...";
    }

    @GetMapping("/login")
    public String testLogin() {
        return "test login...";
    }

    @GetMapping("/internal")
    public String testInternal() {
        return "test internal...";
    }
}
