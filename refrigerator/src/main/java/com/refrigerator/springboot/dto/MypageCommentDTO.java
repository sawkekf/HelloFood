package com.refrigerator.springboot.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import com.refrigerator.springboot.entity.Board;

import lombok.Data;

@Data
public class MypageCommentDTO {
	private LocalDateTime regDate;
	private Long commentid;
	private String content;
	private Board board;
	private String title;
	private String url;
	
	@QueryProjection
	public MypageCommentDTO(LocalDateTime regDate, Long commnetid, String content, Board board,String title, String url) {
		this.regDate = regDate;
		this.commentid = commnetid;
		this.content = content;
		this.board = board;
		this.title = title;
		this.url = url;
	}
}
