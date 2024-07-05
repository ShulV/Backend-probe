package ru.javabegin.micro.demo.commontest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonController {


    @GetMapping("/redirect")
    public String getRedirect() {
        return "page.html";
    }

}
