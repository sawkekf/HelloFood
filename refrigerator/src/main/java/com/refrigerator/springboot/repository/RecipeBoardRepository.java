package com.refrigerator.springboot.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.refrigerator.springboot.dto.ShowBoardDTO;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;

public interface RecipeBoardRepository extends JpaRepository<RecipeBoard, Long>, RecipeBoardCustom{

	@Query("select r from RecipeBoard r where delcheck='N' order by writingid desc")
	Page<RecipeBoard> findByDelCheckIsN(Pageable pageable);

	@Query("select max(writingcount) from RecipeBoard r where delcheck='N'")
	Integer findByAllWriting();

	@Query("select r from RecipeBoard r where title=:title order by writingid desc")
	RecipeBoard findRecipeDetailByTitle(@Param("title") String title);

	@Query("select r from RecipeBoard r where writingId=:writingid")
	RecipeBoard findByRecipeDetail(@Param("writingid") Long writingid);

	@Query("select r from RecipeBoard r where delcheck='N' order by writingid desc")
	List<RecipeBoard> findAllByDelCheckIsN();

	@Query("select r from RecipeBoard r where delcheck='N' order by writingid asc")
	List<RecipeBoard> findAllByDelCheckIsNAndASC();
}