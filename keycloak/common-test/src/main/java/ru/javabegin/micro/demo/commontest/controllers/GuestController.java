package ru.javabegin.micro.demo.commontest.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/guest")
@RestController
public class GuestController {

    @GetMapping("/test")
    @PreAuthorize("permitAll()")
    public String testGuest() {
        return "permit all for guest....";
    }
}
