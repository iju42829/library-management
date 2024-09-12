package com.example.library.library_management.service.member;

import com.example.library.library_management.domain.constants.LoanAccessStatus;

public interface MemberService {
    LoanAccessStatus getMemberLoanAccessStatus(String username);
}
