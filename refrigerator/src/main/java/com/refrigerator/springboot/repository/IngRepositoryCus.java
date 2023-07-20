package com.refrigerator.springboot.repository;

import com.refrigerator.springboot.dto.IngredientDto;
import com.refrigerator.springboot.dto.SmallDto;
import com.refrigerator.springboot.entity.BigTags;
import com.refrigerator.springboot.entity.SmallTags;

import java.util.List;

public interface IngRepositoryCus {
    IngredientDto getIng(Long sid);
    Long updateIng(IngredientDto ingredientDto);
    SmallTags getsmallTag(String id);
    List<SmallDto> getLists(String name);


    Long deleteIng(String str, Long ref);

    Long deleteIngs(String ids, Long id);

    Long updateIng2(IngredientDto ingredientDto);

    List<SmallTags> getSmList();

    List<BigTags> getbiList();

    List<SmallDto> selectByBig(String text);
}
