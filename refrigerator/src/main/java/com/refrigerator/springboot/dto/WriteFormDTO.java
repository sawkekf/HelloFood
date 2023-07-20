package com.refrigerator.springboot.dto;

import com.refrigerator.springboot.constant.NoticeCheck;
import com.refrigerator.springboot.entity.Board;
import com.refrigerator.springboot.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class WriteFormDTO {
	private String title;
	private double kcal;
	private List<String> ingrediant;
	private List<String> ingrediantVol;
	private String cookTip;
	private String difficulty;
	private List<String> recipeContent;
	private NoticeCheck noticeCheck;	
	private Board board;
	private Member member;

}
