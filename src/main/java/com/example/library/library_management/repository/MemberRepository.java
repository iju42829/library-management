package com.example.library.library_management.repository;

import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByUsername(String username);

    Optional<Member> findByUsername(String username);

    List<Member> findAllByLoanAccessStatus(LoanAccessStatus loanAccessStatus);
}
