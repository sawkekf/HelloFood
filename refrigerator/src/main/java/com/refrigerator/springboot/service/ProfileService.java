package com.refrigerator.springboot.service;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import com.refrigerator.springboot.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private MemberRepository memberRepository;


    public void updateProfileImage(String name, String newImg) {


    }
}
