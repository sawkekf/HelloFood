package com.refrigerator.springboot.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.refrigerator.springboot.entity.Board;
import com.refrigerator.springboot.entity.CookBoard;
import com.refrigerator.springboot.entity.Member;

import lombok.Data;

@Data
public class CookCommentDTO {

	private LocalDateTime regDate;
	private Long commentid;
	private String content;
	private Board board;
	private CookBoard cookBoard;
	private Member member;
	
	@QueryProjection
	public CookCommentDTO(LocalDateTime regDate, Long commnetid, String content, Board board,CookBoard cookBoard, Member member) {
		this.regDate = regDate;
		this.commentid = commnetid;
		this.content = content;
		this.board = board;
		this.cookBoard = cookBoard;
		this.member = member;
	}
	
}
