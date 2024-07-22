package ru.javabegin.micro.demo.commontest.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/root")
@RestController
public class RootController {

    @GetMapping("/test")
    @PreAuthorize("hasRole('root')")
    public String testAdmin() {
        return "info only for root role...";
    }
}
