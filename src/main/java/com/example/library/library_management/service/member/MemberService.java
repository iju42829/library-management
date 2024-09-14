package com.example.library.library_management.service.member;

import com.example.library.library_management.domain.Member;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import com.example.library.library_management.exception.member.MemberNotFoundException;

public interface MemberService {

    /**
     * 멤버의 사용자 이름을 통해 멤버의 대출 가능 상태를 조회합니다.
     * @param username 멤버의 사용자 이름
     * @return 멤버의 현재 대출 가능 상태
     * @throws MemberNotFoundException
     * 해당 사용자 이름을 가진 멤버가 존재하지 않는 경우 발생
     */
    LoanAccessStatus getMemberLoanAccessStatus(String username);

    /**
     * 멤버의 사용자 이름을 통해 멤버 정보를 조회합니다.
     * @param username 멤버의 사용자 이름
     * @return 멤버 엔티티
     * @throws MemberNotFoundException
     * 해당 사용자 이름을 가진 멤버가 존재하지 않는 경우 발생
     */
    Member getMemberByUsername(String username);
}
