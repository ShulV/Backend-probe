package ru.javabegin.micro.demo.eurekaclient2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main2")
public class TestController2 {

    @GetMapping("/test2")
    public String test() {
        return "test test2 controller2...";
    }
}
