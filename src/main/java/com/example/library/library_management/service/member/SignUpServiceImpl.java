package com.example.library.library_management.service.member;

import com.example.library.library_management.auth.constants.Role;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.dto.member.request.MemberCreateRequest;
import com.example.library.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Long signUp(MemberCreateRequest memberCreateRequest) {
        Member member = Member.createMember(memberCreateRequest.getUsername(),
                bCryptPasswordEncoder.encode(memberCreateRequest.getPassword()),
                memberCreateRequest.getEmail(),
                Role.USER);

        memberRepository.save(member);

        return member.getId();
    }

    @Override
    public Boolean isUsernameDuplicate(MemberCreateRequest memberCreateRequest) {
        return memberRepository.existsByUsername(memberCreateRequest.getUsername());
    }
}
