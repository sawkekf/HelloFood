package com.refrigerator.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.refrigerator.springboot.entity.RecipeBoard;
import com.refrigerator.springboot.entity.RecipeIngredient;

public interface RecipeIngedientRepository extends JpaRepository<RecipeIngredient, Long>{
	
	@Query("select r from RecipeIngredient r where r.recipeBoard =:recipeBoard")
	List<RecipeIngredient> findByrecipeBoard(@Param("recipeBoard")RecipeBoard reiBoard);
	
}
