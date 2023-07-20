package com.refrigerator.springboot.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeApiDTO2 {
    private Long id;
    private String rep_nm; //레시피 명
    private String info_eng; //칼로리
    private String rcp_part; //재료
    private String manual; //요리 방법


    @QueryProjection
    public RecipeApiDTO2(Long id,String rep_nm, String info_eng, String rcp_part, String manual){
        this.id = id;
        this.rep_nm=rep_nm;
        this.info_eng=info_eng;
        this.rcp_part=rcp_part;
        this.manual=manual;

    }
}
