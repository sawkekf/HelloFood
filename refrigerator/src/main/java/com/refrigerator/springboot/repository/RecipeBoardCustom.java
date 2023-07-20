package com.refrigerator.springboot.repository;

import java.util.List;

import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.RecipeComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;

public interface RecipeBoardCustom {

	Page<ShowBoardDTO> findByDelCheckIsNAndSearch(RecipeSearchDTO recipeSearchDTO, Pageable pageable);

	Page<ShowBoardDTO> myPageDelCheckIsNAndSearch(RecipeSearchDTO recipeSearchDTO, Pageable pageable, Member member);

	Page<ShowBoardDTO> myPageRecipeBook(Pageable pageable, Member member, RecipeSearchDTO recipeSearchDTO);

	Page<MypageCommentDTO> getMyRecipeComment(Member member, Pageable pageable);

	List<ShowBoardDTO> bestRecipe();

	Page<RecipeCommentDTO> getRecipeComment(Long writingId, Pageable pageable);

}
