package com.refrigerator.springboot.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refrigerator.springboot.dto.CookBoardDTO;
import com.refrigerator.springboot.dto.CookCommentDTO;
import com.refrigerator.springboot.dto.CookSearchDTO;
import com.refrigerator.springboot.dto.MypageCommentDTO;
import com.refrigerator.springboot.dto.QCookBoardDTO;
import com.refrigerator.springboot.dto.QCookCommentDTO;
import com.refrigerator.springboot.dto.QMypageCommentDTO;
import com.refrigerator.springboot.constant.Delcheck;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.QCookComment;
import com.refrigerator.springboot.entity.QCookImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class CookBoardCustomImpl implements CookBoardCustom {
	
	private JPAQueryFactory queryFactory;
	
	 public CookBoardCustomImpl(EntityManager em){
	        this.queryFactory = new JPAQueryFactory(em);
	    }

	 private BooleanExpression searchByLike(String searchBy, String searchQuery) {
			
		 if(StringUtils.equals("title", searchBy)) {
			 return QCookImage.cookImage.cookboard.title.like("%"+searchQuery+"%");
		 }else if(StringUtils.equals("nickName", searchBy)) {
			 return QCookImage.cookImage.cookboard.member.nickname.like("%"+searchQuery+"%");
		 }
		 return null;
	 }
	 
	@Override
	public Page<CookBoardDTO> findByDelCheckIsNAndSearch(CookSearchDTO cookSearchDTO, Pageable pageable) {
		List<CookBoardDTO> contents = queryFactory
				.select(new QCookBoardDTO(
						QCookImage.cookImage.cookboard.writingid,
						QCookImage.cookImage.cookboard.writingcount,
						QCookImage.cookImage, 
						QCookImage.cookImage.cookboard.title,
						QCookImage.cookImage.cookboard.member,
						QCookImage.cookImage.cookboard.regdate,
						QCookImage.cookImage.cookboard.seencount,
						QCookImage.cookImage.cookboard.recomcount))
				.from(QCookImage.cookImage)
				.join(QCookImage.cookImage.cookboard)
				.where(QCookImage.cookImage.repimage.like("Y"),QCookImage.cookImage.cookboard.delcheck.eq(Delcheck.N),searchByLike(cookSearchDTO.getSearchBy(), cookSearchDTO.getSearchQuery()))
				.orderBy(QCookImage.cookImage.cookboard.writingid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long total = queryFactory
				.select(Wildcard.count)
				.from(QCookImage.cookImage)
				.join(QCookImage.cookImage.cookboard)
				.where(QCookImage.cookImage.repimage.like("Y"),QCookImage.cookImage.cookboard.delcheck.eq(Delcheck.N),searchByLike(cookSearchDTO.getSearchBy(), cookSearchDTO.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(contents, pageable, total);
				
	}

	@Override
	public Page<CookBoardDTO> myPageDelCheckIsNAndSearch(CookSearchDTO cookSearchDTO, Pageable pageable, Member member) {
		List<CookBoardDTO> contents = queryFactory
				.select(new QCookBoardDTO(
						QCookImage.cookImage.cookboard.writingid,
						QCookImage.cookImage.cookboard.writingcount,
						QCookImage.cookImage, 
						QCookImage.cookImage.cookboard.title,
						QCookImage.cookImage.cookboard.member,
						QCookImage.cookImage.cookboard.regdate,
						QCookImage.cookImage.cookboard.seencount,
						QCookImage.cookImage.cookboard.recomcount))
				.from(QCookImage.cookImage)
				.join(QCookImage.cookImage.cookboard)
				.where(QCookImage.cookImage.repimage.like("Y"),QCookImage.cookImage.cookboard.delcheck.eq(Delcheck.N),QCookImage.cookImage.cookboard.member.eq(member),searchByLike(cookSearchDTO.getSearchBy(), cookSearchDTO.getSearchQuery()))
				.orderBy(QCookImage.cookImage.cookboard.writingid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		Long total = queryFactory
				.select(Wildcard.count)
				.from(QCookImage.cookImage)
				.join(QCookImage.cookImage.cookboard)
				.where(QCookImage.cookImage.repimage.like("Y"),QCookImage.cookImage.cookboard.delcheck.eq(Delcheck.N),QCookImage.cookImage.cookboard.member.eq(member),searchByLike(cookSearchDTO.getSearchBy(), cookSearchDTO.getSearchQuery()))
				.fetchOne();
		
		return new PageImpl<>(contents, pageable, total);
	}

	@Override
	public Page<MypageCommentDTO> getMyCookComment(Member member, Pageable pageable) {
		List<MypageCommentDTO> contents = queryFactory
				.select(new QMypageCommentDTO(
						QCookComment.cookComment.regdate,
						QCookComment.cookComment.commentid,
						QCookComment.cookComment.content,
						QCookComment.cookComment.board,
						QCookComment.cookComment.cookboard.title,
						QCookImage.cookImage.url))
				.from(QCookComment.cookComment)
				.join(QCookImage.cookImage).on(QCookComment.cookComment.cookboard.eq(QCookImage.cookImage.cookboard))
				.where(QCookImage.cookImage.repimage.like("Y"),QCookComment.cookComment.member.eq(member))
				.orderBy(QCookComment.cookComment.commentid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		Long total = queryFactory
				.select(Wildcard.count)
				.from(QCookComment.cookComment)
				.join(QCookImage.cookImage).on(QCookComment.cookComment.cookboard.eq(QCookImage.cookImage.cookboard))
				.where(QCookImage.cookImage.repimage.like("Y"),QCookComment.cookComment.member.eq(member))
				.fetchOne();
		
		return new PageImpl<>(contents,pageable,total);
				
	}

	@Override
	public Page<CookCommentDTO> getCookComment(Long writingId, Pageable pageable) {
		
		List<CookCommentDTO> contents = queryFactory
				.select(new QCookCommentDTO(
						QCookComment.cookComment.regdate, 
						QCookComment.cookComment.commentid, 
						QCookComment.cookComment.content, 
						QCookComment.cookComment.board, 
						QCookComment.cookComment.cookboard, 
						QCookComment.cookComment.member))
				.from(QCookComment.cookComment)
				.where(QCookComment.cookComment.cookboard.writingid.eq(writingId))
				.orderBy(QCookComment.cookComment.commentid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		Long total = queryFactory
				.select(Wildcard.count)
				.from(QCookComment.cookComment)
				.where(QCookComment.cookComment.cookboard.writingid.eq(writingId))
				.fetchOne();
		
		return new PageImpl<>(contents, pageable, total);
				
	}

}
