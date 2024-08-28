package com.example.library.library_management.auth.service;

import com.example.library.library_management.auth.dto.CustomMemberDetails;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(username));

        return new CustomMemberDetails(member);
    }
}
