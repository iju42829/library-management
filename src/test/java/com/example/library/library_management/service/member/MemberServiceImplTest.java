package com.example.library.library_management.service.member;

import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import com.example.library.library_management.dto.member.request.MemberCreateRequest;
import com.example.library.library_management.exception.member.MemberNotFoundException;
import com.example.library.library_management.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceImplTest {

    @Autowired MessageSource ms;

    @Autowired MemberRepository memberRepository;

    @Autowired MemberService memberService;

    @Autowired SignUpService signUpService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() throws IOException  {
        // 멤버1 생성
        String memberJson1 = ms.getMessage("test.member.first", null, Locale.getDefault());
        MemberCreateRequest memberCreateRequest1 = objectMapper.readValue(memberJson1, MemberCreateRequest.class);

        Long memberId1 = signUpService.signUp(memberCreateRequest1);
        member1 = memberRepository.findById(memberId1).orElseThrow(MemberNotFoundException::new);

        // 멤버2 생성
        String memberJson2 = ms.getMessage("test.member.second", null, Locale.getDefault());
        MemberCreateRequest memberCreateRequest2 = objectMapper.readValue(memberJson2, MemberCreateRequest.class);

        Long memberId2 = signUpService.signUp(memberCreateRequest2);

        member2 = memberRepository.findById(memberId2).orElseThrow(MemberNotFoundException::new);
    }

    @Test
    @DisplayName("멤버 대출 가능 여부 조회")
    void getMemberLoanAccessStatus() {
        // given
        member1.changeLoanAccessStatus(LoanAccessStatus.UNAVAILABLE);

        // when
        LoanAccessStatus memberLoanAccessStatus1 = memberService.getMemberLoanAccessStatus(member1.getUsername());
        LoanAccessStatus memberLoanAccessStatus2 = memberService.getMemberLoanAccessStatus(member2.getUsername());

        // then
        assertThat(memberLoanAccessStatus1).isEqualTo(LoanAccessStatus.UNAVAILABLE);
        assertThat(memberLoanAccessStatus2).isEqualTo(LoanAccessStatus.AVAILABLE);
    }

    @Test
    @DisplayName("사용자 이름을 통해 멤버 조회")
    void getMemberByUsername() {
        // when
        Member findMember = memberService.getMemberByUsername(member1.getUsername());

        // then
        assertThat(findMember).isNotNull();
        assertThat(findMember.getUsername()).isEqualTo(member1.getUsername());
        assertThat(findMember.getEmail()).isEqualTo(member1.getEmail());
        assertThat(findMember.getRole()).isEqualTo(member1.getRole());
        assertThat(findMember.getLoanAccessStatus()).isEqualTo(member1.getLoanAccessStatus());
    }
}
