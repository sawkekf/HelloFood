package com.refrigerator.springboot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "RecipeIngredient")
@Getter
@Setter
@ToString
public class RecipeIngredient {
	
	@Id
	@GeneratedValue
	private Long ingId;
	@JoinColumn(name = "writingid")
	@ManyToOne
	private RecipeBoard recipeBoard;
	private String ingrediant;
	private String ingrediantVol;
	
	public static List<RecipeIngredient> newRecipeIngredient(List<String> ingrediants, List<String> ingrediantVols ,RecipeBoard recipeBoard) {
		List<RecipeIngredient> recipeIngredients = new ArrayList<>();
		for(int i=0; i<ingrediants.size(); i++) {
			RecipeIngredient recipeIngredient = new RecipeIngredient();
			recipeIngredient.setRecipeBoard(recipeBoard);
			recipeIngredient.setIngrediant(ingrediants.get(i));
			recipeIngredient.setIngrediantVol(ingrediantVols.get(i));
			recipeIngredients.add(recipeIngredient);
		}
		return recipeIngredients;
	}
}
