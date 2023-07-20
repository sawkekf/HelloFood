package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.constant.LoginType;
import com.refrigerator.springboot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

//    @Query("select m from Member m where m.mem_banned ='Y'")
//    List<Member> bannedMember();

    Member findByEmailAndName(String email, String name);

    Member findByNickname(String nickname);

    Member findByEmailAndLoginType(String email, LoginType loginType);

    List<Member> findByNameAndPhoneNum(String name, String phoneNum);

    Member findByEmailAndLoginTypeAndAndName(String email, LoginType type, String name);

    @Query("select r from Member r where r.email=:email and r.loginType = :logintype")
    Member findmember(@Param("email")String email,@Param("logintype")LoginType loginType);

    @Query("select r from Member r where r.banned='Y'")
    List<Member> findBannedMember();

}