package com.refrigerator.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;
import com.refrigerator.springboot.entity.RecipeRecommend;

public interface RecipeRecommendRepository extends JpaRepository<RecipeRecommend, Long>{

	@Query("select count(r) from RecipeRecommend r where r.recipeboard = :recipeBoard")
	int countRecommend(@Param("recipeBoard") RecipeBoard recipeboard);

	@Query("select count(r) from RecipeRecommend r where r.recipeboard = :recipeBoard and r.member =:member")
	int findByRecipeBoardAndMember(@Param("recipeBoard") RecipeBoard recipeBoard, @Param("member") Member member);

}
