package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.dto.CookBoardDTO;
import com.refrigerator.springboot.dto.CookCommentDTO;
import com.refrigerator.springboot.dto.CookSearchDTO;
import com.refrigerator.springboot.dto.MypageCommentDTO;
import com.refrigerator.springboot.dto.ShowBoardDTO;
import com.refrigerator.springboot.entity.Member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CookBoardCustom {

	Page<CookBoardDTO> findByDelCheckIsNAndSearch(CookSearchDTO cookSearchDTO, Pageable pageable);
	
	Page<CookBoardDTO> myPageDelCheckIsNAndSearch(CookSearchDTO cookSearchDTO, Pageable pageable, Member meber);
	
	Page<MypageCommentDTO> getMyCookComment(Member member, Pageable pageable);
	
	Page<CookCommentDTO> getCookComment(Long writingId, Pageable pageable);
	
}
