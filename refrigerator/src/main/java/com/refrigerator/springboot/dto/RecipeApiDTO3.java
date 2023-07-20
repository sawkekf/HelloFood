package com.refrigerator.springboot.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RecipeApiDTO3 {
    private Long id;
    private String rep_nm; //레시피 명
    private String info_eng; //칼로리
    private String rcp_part; //재료
    private String manual; //요리 방법
    private Integer count;
    private String count_str;
    
    public RecipeApiDTO3(RecipeApiDTO re){
        this.rep_nm = re.getRep_nm();
        this.info_eng = re.getInfo_eng();
        this.rcp_part = re.getRcp_part();
        this.manual= re.getManual();
        this.count_str = "";
        this.id=re.getId();

    }

    @QueryProjection
    public RecipeApiDTO3(Long id,String rep_nm, String info_eng, String rcp_part, String manual){
        this.id = id;
        this.rep_nm=rep_nm;
        this.info_eng=info_eng;
        this.rcp_part=rcp_part;
        this.manual=manual;

    }
}