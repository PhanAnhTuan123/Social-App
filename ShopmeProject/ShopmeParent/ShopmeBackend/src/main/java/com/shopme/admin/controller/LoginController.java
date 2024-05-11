package com.shopme.admin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class LoginController {
    @GetMapping("/showLoginPage")
    public String showLoginPage(){
        return "login";
    }
    @GetMapping("/access-denied")
    public String showAccessDenied(){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Don't Access!!!");
    }
}
