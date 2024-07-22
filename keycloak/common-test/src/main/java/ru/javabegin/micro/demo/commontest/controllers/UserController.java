package ru.javabegin.micro.demo.commontest.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {
    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('root', 'admin', 'user')")
    public String testUser() {
        return "info only for admin and user roles...";
    }
}
