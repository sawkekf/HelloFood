package com.refrigerator.springboot.service;


import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordService {
    private final MemberRepository memberRepository;

    public PasswordService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean checkCurrentPassword(String memMail, String currentPassword){
        Member member = memberRepository.findByEmail(memMail);
        return passwordEncoder.matches(currentPassword,member.getPw());
    }
    public void changePassword(String memMail, String newPassword){
        Member member = memberRepository.findByEmail(memMail);
        member.setPw(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }



}
