package com.refrigerator.springboot.service;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class NicknameService {
    private final MemberRepository memberRepository;

    public NicknameService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public void changeNickname(String memMail, String newNickname){
        Member member = memberRepository.findByEmail(memMail);
        if(member == null){
            throw new IllegalArgumentException("로그인되지 않았습니다.");
        }

        if(!member.getNickname().equals(newNickname)){
            Member existingMem = memberRepository.findByEmail(newNickname);
            if(existingMem != null){
                throw new IllegalArgumentException("해당 닉네임이 이미 존재합니다.");
            }
            member.setNickname(newNickname);
            memberRepository.save(member);
        }
    }
}
