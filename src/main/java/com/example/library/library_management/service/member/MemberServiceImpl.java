package com.example.library.library_management.service.member;

import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import com.example.library.library_management.exception.member.MemberNotFoundException;
import com.example.library.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@Transactional
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

    @Override
    public Member getMemberByUsername(String username) {
        return memberRepository
                .findByUsername(username)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Scheduled(cron = "0 30 18 * * ?")
    public void unlockMemberLoanAccess() {
        List<Member> members = memberRepository
                .findAllByLoanAccessStatus(LoanAccessStatus.UNAVAILABLE);

        for (Member member : members) {
            if (member.getReservationLockDate().isEqual(LocalDate.now()) ||
                    member.getReservationLockDate().isBefore(LocalDate.now())) {

                member.setLoanAccessStatus(LoanAccessStatus.AVAILABLE);
            }
        }
    }
}
