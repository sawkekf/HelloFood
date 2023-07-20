package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.entity.RecipeAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

	
public interface RecipeApiRepository extends JpaRepository<RecipeAPI,Long> {
	  @Query("select r from RecipeAPI r where r.id=:id")
	  RecipeAPI getRecipefindByName(@Param("id") Long id);
	  @Query("select count(r) from RecipeAPI r where r.id=:id")
	  int getRecipefindBy(@Param("id") Long id);
}
	

