package com.refrigerator.springboot.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmallDto {


    private Long id;

    private String tag_small_name;

    private Long bigTagID;

    @QueryProjection
    public SmallDto(Long id,String tag_small_name,Long bigTagID){
        this.id = id;
        this.tag_small_name = tag_small_name;
        this.bigTagID = bigTagID;
    }
}
