package com.example.library.library_management.controller.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login(@RequestParam(required = false) Boolean error, Model model) {

        if (error != null) {
            model.addAttribute("loginError", true);
            return "member/login";
        }

        return "member/login";
    }
}
