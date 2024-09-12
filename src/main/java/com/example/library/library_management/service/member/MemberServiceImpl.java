package com.example.library.library_management.service.member;

import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import com.example.library.library_management.exception.member.MemberNotFoundException;
import com.example.library.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public LoanAccessStatus getMemberLoanAccessStatus(String username) {
        Member member = memberRepository
                .findByUsername(username)
                .orElseThrow(MemberNotFoundException::new);

        return member.getLoanAccessStatus();
    }
}
