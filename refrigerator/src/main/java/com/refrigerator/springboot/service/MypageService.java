package com.refrigerator.springboot.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refrigerator.springboot.dto.MypageCommentDTO;
import com.refrigerator.springboot.entity.CookComment;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.MemberImg;
import com.refrigerator.springboot.entity.RecipeComment;
import com.refrigerator.springboot.entity.TemporaryImage;
import com.refrigerator.springboot.repository.CookBoardCustomImpl;
import com.refrigerator.springboot.repository.CookCommentRepository;
import com.refrigerator.springboot.repository.CookImageRepository;
import com.refrigerator.springboot.repository.MemImgRepository;
import com.refrigerator.springboot.repository.RecipeCommentRepository;
import com.refrigerator.springboot.repository.RecipeImageRepository;
import com.refrigerator.springboot.repository.TemporaryImageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MypageService {
	
	final RecipeCommentRepository recipeCommentRepository;
	final CookCommentRepository cookCommentRepository;
	final RecipeImageRepository recipeImageRepository;
	final CookImageRepository cookImageRepository;
	final FileService fileService;
	final TemporaryImageRepository temporaryImageRepository;
	final MemImgRepository memImgRepository;
	
	public Page<MypageCommentDTO> getMyRecipeComment(Member member ,Pageable pageable) {
		
		List<RecipeComment> myRecipeComments = recipeCommentRepository.findMyRecipeComments(member);
		Long total = recipeCommentRepository.findMyRecipeCount(member);
		List<MypageCommentDTO> mypageCommentDTOs = new ArrayList<>();
		
//		for(RecipeComment recipeComment : myRecipeComments) {
//			MypageCommentDTO mypageCommentDTO = new MypageCommentDTO();
//			mypageCommentDTO.setTitle(recipeComment.getRecipeboard().getTitle());
//			mypageCommentDTO.setContent(recipeComment.getContent());
//			mypageCommentDTO.setRegDate(recipeComment.getRegdate());
//			mypageCommentDTO.setBoard(recipeComment.getBoard());
//			mypageCommentDTO.setCommentid(recipeComment.getCommentid());
//			mypageCommentDTO.setUrl(recipeImageRepository.findRepUrl(recipeComment.getRecipeboard()));
//			mypageCommentDTOs.add(mypageCommentDTO);
//		}
		
		System.out.println(total);
		return new PageImpl<>(mypageCommentDTOs,pageable,total);
		
	}
	
public Page<MypageCommentDTO> getMyCookComment(Member member ,Pageable pageable) {
		
		List<CookComment> myCookComments = cookCommentRepository.findMyCookComment(member);
		Long total = cookCommentRepository.findMyCookCommentsCount(member);
		List<MypageCommentDTO> mypageCommentDTOs = new ArrayList<>();
		
//		for(CookComment cookComment : myCookComments) {
//			MypageCommentDTO mypageCommentDTO = new MypageCommentDTO();
//			mypageCommentDTO.setTitle(cookComment.getCookboard().getTitle());
//			mypageCommentDTO.setContent(cookComment.getContent());
//			mypageCommentDTO.setRegDate(cookComment.getRegdate());
//			mypageCommentDTO.setBoard(cookComment.getBoard());
//			mypageCommentDTO.setCommentid(cookComment.getCommentid());
//			mypageCommentDTO.setUrl(cookImageRepository.findRepUrl(cookComment.getCookboard()));
//			mypageCommentDTOs.add(mypageCommentDTO);
//		}
		System.out.println(total);
		return new PageImpl<>(mypageCommentDTOs,pageable,total);
		
	}

public String saveTemporaryImage2(MultipartFile Image) {
	TemporaryImage temporaryImage = new TemporaryImage();
	String uploadPath = "C:/storage/project";
	String oriName = Image.getOriginalFilename();
	String imgName = "";
	String url = "";
	try {
		if(!StringUtils.isEmpty(oriName)) {
			imgName = fileService.uploadFile(uploadPath, oriName, Image.getBytes());
			url="/main/project/"+imgName;
		}
		
		temporaryImage.createImage(imgName, oriName, url);
		temporaryImageRepository.save(temporaryImage);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return url;
}

public void updateProfileImage(MultipartFile image,Member member) {
	MemberImg memberImg = new MemberImg();
	MemberImg postImg = memImgRepository.findByMember(member);
	if(postImg!=null) {
		memImgRepository.delete(postImg);
	}
	TemporaryImage temporaryImage = new TemporaryImage();
	String uploadPath = "C:/storage/project";
	String oriName = image.getOriginalFilename();
	String imgName = "";
	String url = "";
	try {
		if(!StringUtils.isEmpty(oriName)) {
			imgName = fileService.uploadFile(uploadPath, oriName, image.getBytes());
			url="/main/project/"+imgName;
		}
		memberImg.updateMemImg(oriName, imgName, url);
		memberImg.setMember(member);
		memImgRepository.save(memberImg);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	
}
