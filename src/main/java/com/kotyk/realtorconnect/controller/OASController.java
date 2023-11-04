package com.kotyk.realtorconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OASController {

    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }

}
