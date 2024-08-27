package com.example.library.library_management.service.member;

import com.example.library.library_management.dto.member.request.MemberCreateRequest;

public interface SignUpService {
    Long signUp(MemberCreateRequest memberCreateRequest);

    Boolean isUsernameDuplicate(MemberCreateRequest memberCreateRequest);
}
