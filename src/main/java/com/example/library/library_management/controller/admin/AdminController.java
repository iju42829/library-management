package com.example.library.library_management.controller.admin;

import com.example.library.library_management.controller.argumentResolver.Login;
import com.example.library.library_management.dto.member.MemberSessionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @ModelAttribute("memberSessionDto")
    public MemberSessionDto addMemberSessionDto(@Login MemberSessionDto memberSessionDto) {
        return memberSessionDto;
    }

    @GetMapping
    public String showDashboard() {
        return "admin/dashboard";
    }
}
