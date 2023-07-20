package com.refrigerator.springboot.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.refrigerator.springboot.dto.*;
import com.refrigerator.springboot.entity.QRecipeAPI;
import com.refrigerator.springboot.entity.QSmallTags;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
@Repository
public class ReciperepositoryCusImp
{
    private JPAQueryFactory queryFactory;

    public ReciperepositoryCusImp(EntityManager em){  this.queryFactory = new JPAQueryFactory(em);  }


    public List<RecipeApiDTO2> getAll() {
        QRecipeAPI qrecipeAPI = QRecipeAPI.recipeAPI;
        QSmallTags smallTags = QSmallTags.smallTags;


        List<RecipeApiDTO2> ingredientDtos =
                queryFactory
                        .select(new QRecipeApiDTO2(qrecipeAPI.id,qrecipeAPI.rep_nm,qrecipeAPI.info_eng,qrecipeAPI.rcp_part,qrecipeAPI.manual))
                        .from(qrecipeAPI,smallTags)
                        .fetch();

        return ingredientDtos;
    }

    public List<RecipeApiDTO3> SelectName(String sad) {
        QRecipeAPI qrecipeAPI = QRecipeAPI.recipeAPI;
        System.out.println(sad);

        List<RecipeApiDTO3> ingredientDtos =
                queryFactory
                        .select(new QRecipeApiDTO3(qrecipeAPI.id,qrecipeAPI.rep_nm,qrecipeAPI.info_eng,qrecipeAPI.rcp_part,qrecipeAPI.manual))
                        .from(qrecipeAPI)
                        .where(qrecipeAPI.rcp_part.contains(sad))
                        .fetch();

        return ingredientDtos;
    }
}
