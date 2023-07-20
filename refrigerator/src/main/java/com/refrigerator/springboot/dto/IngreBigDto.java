package com.refrigerator.springboot.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import com.refrigerator.springboot.entity.BigTags;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@ToString
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IngreBigDto {
    private Long id;

    private LocalDateTime ing_deadline;

    private String smallTags;

    private Long Big;

    private String srcs;
    @JsonIgnore
    private BigTags bigTags;

    private String memo ;

    @QueryProjection
    public IngreBigDto(Long id,LocalDateTime ing_deadline,String smalltags,Long big ,String src,BigTags bigtags,String memo){
        this.id = id;
        this.ing_deadline = ing_deadline;
        this.smallTags = smalltags;

        this.Big = big;
        this.srcs = src;
        this.bigTags = bigtags;
        this.memo = memo;
    }
}
