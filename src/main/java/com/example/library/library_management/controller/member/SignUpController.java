package com.example.library.library_management.controller.member;

import com.example.library.library_management.controller.validator.MemberCreateRequestValidator;
import com.example.library.library_management.dto.member.request.MemberCreateRequest;
import com.example.library.library_management.service.member.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/signUp")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;
    private final MemberCreateRequestValidator memberCreateRequestValidator;

    @GetMapping
    public String showSignUpForm(Model model) {
        model.addAttribute("memberCreateRequest", new MemberCreateRequest());

        return "member/sign-up";
    }

    @PostMapping
    public String addMember(@Validated @ModelAttribute MemberCreateRequest memberCreateRequest,
                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "member/sign-up";
        }

        // username 중복 체크
        if (signUpService.isUsernameDuplicate(memberCreateRequest)) {
            bindingResult.rejectValue("username", "duplicate");

            log.info("errors={}", bindingResult);

            return "member/sign-up";
        }

        // 비밀번호 일치 체크
        memberCreateRequestValidator.validate(memberCreateRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);

            return "member/sign-up";
        }

        signUpService.signUp(memberCreateRequest);

        return "redirect:/";
    }
}
