package com.example.library.library_management.domain;

import com.example.library.library_management.auth.constants.Role;
import com.example.library.library_management.domain.base.BaseTimeEntity;
import com.example.library.library_management.domain.constants.LoanAccessStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Role role;

    private LocalDate reservationLockDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanAccessStatus loanAccessStatus;

    private Member(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.loanAccessStatus = LoanAccessStatus.AVAILABLE;
    }

    public static Member createMember(String username, String password, String email, Role role) {
        return new Member(username, password, email, role);
    }

    public void changeLoanAccessStatus(LoanAccessStatus loanAccessStatus) {
        this.loanAccessStatus = loanAccessStatus;
    }

    public void changeReservationLockDate(LocalDate reservationLockDate) {
        this.reservationLockDate = reservationLockDate;
    }
}
