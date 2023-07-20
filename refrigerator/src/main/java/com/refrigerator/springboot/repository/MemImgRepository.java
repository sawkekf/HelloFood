package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemImgRepository extends JpaRepository<MemberImg,Long> {

	MemberImg findByMember(Member member);

}
