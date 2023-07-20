package com.refrigerator.springboot.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.refrigerator.springboot.entity.Board;
import com.refrigerator.springboot.entity.Member;
import com.refrigerator.springboot.entity.RecipeBoard;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeCommentDTO {

    private Long commentid;
    private Member member;
    private RecipeBoard recipeboard;
    private String content;
    private LocalDateTime regdate;
    private Board board;

    @QueryProjection
    public RecipeCommentDTO(Long commentid, Member member, RecipeBoard recipeBoard, String content, LocalDateTime regdate, Board board){
        this.commentid = commentid;
        this.member = member;
        this.recipeboard = recipeBoard;
        this.content = content;
        this.regdate = regdate;
        this.board = board;
    }

}
