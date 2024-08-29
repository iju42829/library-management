package com.example.library.library_management.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSessionDto {

    private String username;
    private Boolean isAdmin;
}
