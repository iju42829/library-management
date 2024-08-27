package com.example.library.library_management.controller.validator;

import com.example.library.library_management.dto.member.request.MemberCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class MemberCreateRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberCreateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberCreateRequest memberCreateRequest = (MemberCreateRequest) target;

        if (!errors.hasErrors()) {
            if (!memberCreateRequest.getPassword().equals(memberCreateRequest.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "MisMatch");
            }
        }
    }
}
