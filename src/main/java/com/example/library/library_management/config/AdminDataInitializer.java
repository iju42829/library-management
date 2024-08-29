package com.example.library.library_management.config;

import com.example.library.library_management.auth.constants.Role;
import com.example.library.library_management.domain.Member;
import com.example.library.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDataInitializer {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${admin.name}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.email}")
    private String email;

    @EventListener(ApplicationReadyEvent.class)
    public void initAdmin() {
        long count = memberRepository.count();

        if (count == 0) {
            Member admin = Member.createMember(username,
                    bCryptPasswordEncoder.encode(password),
                    email,
                    Role.ADMIN);

            memberRepository.save(admin);
        }
    }
}
