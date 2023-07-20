package com.refrigerator.springboot.repository;


import com.refrigerator.springboot.dto.IngredientDto;
import com.refrigerator.springboot.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngRepository  extends JpaRepository<Ingredient,Long> {

    @Query(value = "select * from ingredient where ref_id = :id  ", nativeQuery = true)
    List<Ingredient> allList(@Param("id")Long id);



}
