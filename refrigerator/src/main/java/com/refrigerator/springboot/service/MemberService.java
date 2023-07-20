package com.refrigerator.springboot.service;

import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import com.refrigerator.springboot.repository.MemImgRepository;
import com.refrigerator.springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final String memberImgLocation = "C:/storage/image";
    private final MemberRepository memberRepository;
    private final MemImgRepository memImgRepository;
    private final FileService fileService;
    private final PasswordEncoder encoder;

    private String name;
    private String email;
    private String nickname;
    private LoginType loginType;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmailAndLoginType(email, LoginType.NOMAL);
        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPw())
                .roles(member.getRole().toString())
                .build();
    }

    public Member load(String memName){
        return memberRepository.findByEmail(memName);
    }

    public Member getCurrentMember(String memName) {


        Member member = memberRepository.findByEmail(memName);

        return member;
    }


    public void saveMember(MemberImg memberImg, MultipartFile profileImg) throws Exception {
        String oldImg = profileImg.getOriginalFilename();
        String newImg = "";
        String imgUrl = "";

        if (!StringUtils.isEmpty(oldImg)) {
            newImg = fileService.uploadFile(memberImgLocation, oldImg, profileImg.getBytes());
            imgUrl = "C:/storage/image/" + newImg;
        }
        memberImg.updateMemImg(oldImg, newImg, imgUrl);
        memImgRepository.save(memberImg);
    }
    public void updateMember(Long memImgId,MultipartFile profileImg) throws Exception{
        if(!profileImg.isEmpty()){
            MemberImg saveProfile = memImgRepository.findById(memImgId)
                    .orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(saveProfile.getNewImg())){
                fileService.deleteFile(memberImgLocation+"/"+saveProfile.getNewImg());
            }
            String oldImg = profileImg.getOriginalFilename();
            String newImg = fileService.uploadFile(memberImgLocation,oldImg
                    , profileImg.getBytes());
            String imgUrl = "C:/storage/image/"+newImg;
            saveProfile.updateMemImg(oldImg,newImg,imgUrl);
        }
    }

}

