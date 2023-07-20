package com.refrigerator.springboot.repository;



import java.util.List;

import javax.persistence.EntityManager;

import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refrigerator.springboot.constant.Delcheck;

public class RecipeBoardCustomImpl implements RecipeBoardCustom{

	private JPAQueryFactory queryFactory;

	public RecipeBoardCustomImpl(EntityManager em){
		this.queryFactory = new JPAQueryFactory(em);
	}

	private BooleanExpression searchByLike(String searchBy, String searchQuery) {

		if(StringUtils.equals("title", searchBy)) {
			return QRecipeImage.recipeImage.recipeboard.title.like("%"+searchQuery+"%");
		}else if(StringUtils.equals("nickName", searchBy)) {
			return QRecipeImage.recipeImage.recipeboard.member.nickname.eq(searchQuery);
		}
		return null;
	}

	@Override
	public Page<ShowBoardDTO> findByDelCheckIsNAndSearch(RecipeSearchDTO recipeSearchDTO, Pageable pageable) {
		List<ShowBoardDTO> contents = queryFactory
				.select(new QShowBoardDTO(
						QRecipeImage.recipeImage.recipeboard.writingid,
						QRecipeImage.recipeImage.recipeboard.writingcount,
						QRecipeImage.recipeImage,
						QRecipeImage.recipeImage.recipeboard.title,
						QRecipeImage.recipeImage.recipeboard.difficulty,
						QRecipeImage.recipeImage.recipeboard.member,
						QRecipeImage.recipeImage.recipeboard.regdate,
						QRecipeImage.recipeImage.recipeboard.seencount,
						QRecipeImage.recipeImage.recipeboard.recomcount))
				.from(QRecipeImage.recipeImage)
				.join(QRecipeImage.recipeImage.recipeboard)
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeImage.recipeImage.recipeboard.delcheck.eq(Delcheck.N),searchByLike(recipeSearchDTO.getSearchBy(), recipeSearchDTO.getSearchQuery()))
				.orderBy(QRecipeImage.recipeImage.recipeboard.writingid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(Wildcard.count)
				.from(QRecipeImage.recipeImage)
				.join(QRecipeImage.recipeImage.recipeboard)
				.where(QRecipeImage.recipeImage.repimage.like("Y"), QRecipeImage.recipeImage.recipeboard.delcheck.eq(Delcheck.N),searchByLike(recipeSearchDTO.getSearchBy(), recipeSearchDTO.getSearchQuery()))
				.fetchOne();
		System.out.println(total);
		return new PageImpl<>(contents, pageable, total);
	}

	@Override
	public Page<ShowBoardDTO> myPageDelCheckIsNAndSearch(RecipeSearchDTO recipeSearchDTO, Pageable pageable, Member member) {
		List<ShowBoardDTO> contents = queryFactory
				.select(new QShowBoardDTO(
						QRecipeImage.recipeImage.recipeboard.writingid,
						QRecipeImage.recipeImage.recipeboard.writingcount,
						QRecipeImage.recipeImage,
						QRecipeImage.recipeImage.recipeboard.title,
						QRecipeImage.recipeImage.recipeboard.difficulty,
						QRecipeImage.recipeImage.recipeboard.member,
						QRecipeImage.recipeImage.recipeboard.regdate,
						QRecipeImage.recipeImage.recipeboard.seencount,
						QRecipeImage.recipeImage.recipeboard.recomcount))
				.from(QRecipeImage.recipeImage)
				.join(QRecipeImage.recipeImage.recipeboard)
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeImage.recipeImage.recipeboard.delcheck.eq(Delcheck.N),QRecipeImage.recipeImage.recipeboard.member.eq(member),searchByLike(recipeSearchDTO.getSearchBy(), recipeSearchDTO.getSearchQuery()))
				.orderBy(QRecipeImage.recipeImage.recipeboard.writingid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(Wildcard.count)
				.from(QRecipeImage.recipeImage)
				.join(QRecipeImage.recipeImage.recipeboard)
				.where(QRecipeImage.recipeImage.repimage.like("Y"), QRecipeImage.recipeImage.recipeboard.delcheck.eq(Delcheck.N),QRecipeImage.recipeImage.recipeboard.member.eq(member),searchByLike(recipeSearchDTO.getSearchBy(), recipeSearchDTO.getSearchQuery()))
				.fetchOne();
		System.out.println(total);
		return new PageImpl<>(contents, pageable, total);
	}

	public Page<ShowBoardDTO> myPageRecipeBook(Pageable pageable, Member member, RecipeSearchDTO recipeSearchDTO) {
		List<ShowBoardDTO> contents = queryFactory
				.select(new QShowBoardDTO(
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.writingid,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.writingcount,
						QRecipeImage.recipeImage,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.title,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.difficulty,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.member,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.regdate,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.seencount,
						QRecipeFollowWriting.recipeFollowWriting.recipeboard.recomcount))
				.from(QRecipeFollowWriting.recipeFollowWriting)
				.join(QRecipeImage.recipeImage).on(QRecipeFollowWriting.recipeFollowWriting.recipeboard.eq(QRecipeImage.recipeImage.recipeboard))
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeFollowWriting.recipeFollowWriting.recipeboard.delcheck.eq(Delcheck.N),QRecipeFollowWriting.recipeFollowWriting.member.eq(member),searchByLike("title", recipeSearchDTO.getSearchQuery()))
				.orderBy(QRecipeFollowWriting.recipeFollowWriting.recipeboard.writingid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(Wildcard.count)
				.from(QRecipeFollowWriting.recipeFollowWriting)
				.join(QRecipeImage.recipeImage).on(QRecipeFollowWriting.recipeFollowWriting.recipeboard.eq(QRecipeImage.recipeImage.recipeboard))
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeFollowWriting.recipeFollowWriting.recipeboard.delcheck.eq(Delcheck.N),QRecipeFollowWriting.recipeFollowWriting.member.eq(member),searchByLike("title", recipeSearchDTO.getSearchQuery()))
				.fetchOne();
		System.out.println(total);
		return new PageImpl<>(contents, pageable, total);
	}

	@Override
	public Page<MypageCommentDTO> getMyRecipeComment(Member member, Pageable pageable) {
		List<MypageCommentDTO> contents = queryFactory
				.select(new QMypageCommentDTO(
						QRecipeComment.recipeComment.regdate,
						QRecipeComment.recipeComment.commentid,
						QRecipeComment.recipeComment.content,
						QRecipeComment.recipeComment.board,
						QRecipeComment.recipeComment.recipeboard.title,
						QRecipeImage.recipeImage.url))
				.from(QRecipeComment.recipeComment)
				.join(QRecipeImage.recipeImage).on(QRecipeComment.recipeComment.recipeboard.eq(QRecipeImage.recipeImage.recipeboard))
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeComment.recipeComment.member.eq(member))
				.orderBy(QRecipeComment.recipeComment.commentid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(Wildcard.count)
				.from(QRecipeComment.recipeComment)
				.join(QRecipeImage.recipeImage).on(QRecipeComment.recipeComment.recipeboard.eq(QRecipeImage.recipeImage.recipeboard))
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeComment.recipeComment.member.eq(member))
				.fetchOne();
		return new PageImpl<>(contents, pageable, total);
	}

	@Override
	public List<ShowBoardDTO> bestRecipe() {
		List<ShowBoardDTO> bestRecipe = queryFactory
				.select(new QShowBoardDTO(
						QRecipeImage.recipeImage.recipeboard.writingid,
						QRecipeImage.recipeImage.recipeboard.writingcount,
						QRecipeImage.recipeImage,
						QRecipeImage.recipeImage.recipeboard.title,
						QRecipeImage.recipeImage.recipeboard.difficulty,
						QRecipeImage.recipeImage.recipeboard.member,
						QRecipeImage.recipeImage.recipeboard.regdate,
						QRecipeImage.recipeImage.recipeboard.seencount,
						QRecipeImage.recipeImage.recipeboard.recomcount))
				.from(QRecipeImage.recipeImage)
				.join(QRecipeImage.recipeImage.recipeboard)
				.where(QRecipeImage.recipeImage.repimage.like("Y"),QRecipeImage.recipeImage.recipeboard.delcheck.eq(Delcheck.N))
				.orderBy(QRecipeImage.recipeImage.recipeboard.dailycount.desc())
				.limit(3l)
				.fetch();
		return bestRecipe;
	}

	@Override
	public Page<RecipeCommentDTO> getRecipeComment(Long writingId, Pageable pageable) {

		List<RecipeCommentDTO> contents = queryFactory
				.select(new QRecipeCommentDTO(
						QRecipeComment.recipeComment.commentid,
						QRecipeComment.recipeComment.member,
						QRecipeComment.recipeComment.recipeboard,
						QRecipeComment.recipeComment.content,
						QRecipeComment.recipeComment.regdate,
						QRecipeComment.recipeComment.board
				))
				.from(QRecipeComment.recipeComment)
				.where(QRecipeComment.recipeComment.recipeboard.writingid.eq(writingId))
				.orderBy(QRecipeComment.recipeComment.commentid.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(Wildcard.count)
				.from(QRecipeComment.recipeComment)
				.where(QRecipeComment.recipeComment.recipeboard.writingid.eq(writingId))
				.fetchOne();

		return new PageImpl<>(contents,pageable,total);

	}


}