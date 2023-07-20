package com.refrigerator.springboot.dto;

import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.constant.Role;
import com.refrigerator.springboot.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MemberDto {
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String phoneNum;
    private LoginType loginType;
    private Role role;
    public MemberDto(Member user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPw();
        this.nickname = user.getNickname();
        this.phoneNum = user.getPhoneNum();
        this.loginType = user.getLoginType();
    }

    public MemberDto(String nickname, String email, String name, LoginType type) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.loginType = type;
    }
}
