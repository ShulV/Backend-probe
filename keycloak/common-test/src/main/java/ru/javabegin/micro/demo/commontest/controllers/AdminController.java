package ru.javabegin.micro.demo.commontest.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('admin', 'root')")
    public String testAdmin() {
        return "info only for admin role...";
    }
}
