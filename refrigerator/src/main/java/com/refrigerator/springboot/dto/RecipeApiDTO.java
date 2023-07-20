package com.refrigerator.springboot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeApiDTO {
    private String rep_nm;
    private String info_eng;
    private String rcp_part;
    private String manual;
    private Long id;

    public RecipeApiDTO(String rep_nm, String info_eng, String rcp_part, String manual){
        this.rep_nm=rep_nm;
        this.info_eng=info_eng;
        this.rcp_part=rcp_part;
        this.manual=manual;

    }
    public RecipeApiDTO(String rep_nm, String info_eng, String rcp_part, String manual,Long id){
        this.rep_nm=rep_nm;
        this.info_eng=info_eng;
        this.rcp_part=rcp_part;
        this.manual=manual;
        this.id = id;
    }	
}
