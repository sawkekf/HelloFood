package com.refrigerator.springboot.dto;

import com.refrigerator.springboot.entity.RecipeBoard;
import com.refrigerator.springboot.entity.RecipeContent;
import com.refrigerator.springboot.entity.RecipeImage;
import com.refrigerator.springboot.entity.RecipeIngredient;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class DetailViewDTO {

	private RecipeBoard recipeBoard;
	List<RecipeContent> recipeContents;
	RecipeImage recipeImage;
	Page<RecipeCommentDTO> recipeComments;
	List<RecipeIngredient> recipeIngredients;
	
}
