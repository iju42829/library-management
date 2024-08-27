package com.example.library.library_management.repository;

import com.example.library.library_management.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByUsername(String username);
}
