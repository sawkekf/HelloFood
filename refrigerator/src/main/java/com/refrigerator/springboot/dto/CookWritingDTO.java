package com.refrigerator.springboot.dto;

import com.refrigerator.springboot.constant.NoticeCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CookWritingDTO {
	
	@NotBlank(message = "제목을 입력해주세요")
	private String title;
	@NotBlank(message = "내용을 입력해주세요")
	private String content;
	private NoticeCheck noticeCheck;
	
}
