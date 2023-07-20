package com.refrigerator.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;
import com.refrigerator.springboot.entity.RecipeComment;

public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {

	@Query("select c from RecipeComment c where c.recipeboard = :recipeBoard order by commentid asc")
	List<RecipeComment> findRecipeCommentByWritingId(@Param("recipeBoard") RecipeBoard recipeBoard);

	@Query("select c from RecipeComment c where c.recipeboard = :recipeBoard and c.commentid =:commentid")
	RecipeComment findCommentByRecipeAndCommentId(@Param("recipeBoard") RecipeBoard recipeboard, @Param("commentid") Long commentid);

	@Query("select c from RecipeComment c where c.commentid =:commentid")
	RecipeComment findCommentByCommentId(@Param("commentid") Long commentid);
	
	@Query("select c from RecipeComment c where c.member =:member order by regdate desc")
	List<RecipeComment> findMyRecipeComments(@Param("member") Member member);
	
	@Query("select count(c) from RecipeComment c where c.member =:member")
	Long findMyRecipeCount (@Param("member") Member member);
	
}
