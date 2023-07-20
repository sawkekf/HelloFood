package com.refrigerator.springboot.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class IngredientDto {
    private Long id;

    private LocalDateTime ing_deadline;

    private String smallTags;

    private LocalDateTime regTime;

    private String texts;
    @QueryProjection
    public IngredientDto(Long id, LocalDateTime ing_deadline, String smalltags, LocalDateTime regTime, String texts){
        this.id = id;
        this.ing_deadline = ing_deadline;
        this.smallTags = smalltags;
        this.regTime = regTime;
        this.texts = texts;

    }


}
