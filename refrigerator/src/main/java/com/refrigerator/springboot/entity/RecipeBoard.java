package com.refrigerator.springboot.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Autowired;

import com.refrigerator.springboot.dto.WriteFormDTO;
import com.refrigerator.springboot.constant.Delcheck;
import com.refrigerator.springboot.constant.NoticeCheck;
import com.refrigerator.springboot.repository.BoardRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "RecipeBoard")
@Getter
@Setter
@ToString

public class RecipeBoard {

	@Id
	@GeneratedValue
	private Long writingid;
	private int writingcount;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="mem_id")
	private Member member;
	private String title;
	@OneToMany(mappedBy = "recipeboard")
	private List<RecipeContent> recipecontent = new ArrayList<>();
	@OneToMany(mappedBy = "recipeboard")
	private List<RecipeImage> recipeimage = new ArrayList<>();
	private LocalDateTime regdate;
	@ColumnDefault("0")
	private int seencount;
	@ColumnDefault("0")
	private int recomcount;
	@Enumerated(EnumType.STRING)
	private Delcheck delcheck;
	@Enumerated(EnumType.STRING)
	private NoticeCheck noticecheck;
	@ColumnDefault("0")
	private double kcal;
	private String difficulty;
	private String cooktip;
	@ColumnDefault("0")
	private int exceptdaily;
	@ColumnDefault("0")
	private int dailycount;
	@ManyToOne
	@JoinColumn(name = "boardid")
	private Board board;

	public static RecipeBoard writing(WriteFormDTO writeFormDTO ,Integer writingCon) {
		RecipeBoard recipeBoard = new RecipeBoard();
		recipeBoard.setWritingcount(writingCon);
		recipeBoard.setTitle(writeFormDTO.getTitle());
		recipeBoard.setKcal(writeFormDTO.getKcal());
		recipeBoard.setDifficulty(writeFormDTO.getDifficulty());
		recipeBoard.setCooktip(writeFormDTO.getCookTip());
		recipeBoard.setRegdate(LocalDateTime.now());
		recipeBoard.setDelcheck(Delcheck.N);
		recipeBoard.setNoticecheck(writeFormDTO.getNoticeCheck());
		recipeBoard.setMember(writeFormDTO.getMember());
		recipeBoard.setBoard(writeFormDTO.getBoard());
		return recipeBoard;
	}

	public void updateWriting(WriteFormDTO writeFormDTO) {
		this.setTitle(writeFormDTO.getTitle());
		this.setKcal(writeFormDTO.getKcal());
		this.setDifficulty(writeFormDTO.getDifficulty());
		this.setNoticecheck(writeFormDTO.getNoticeCheck());
		this.setCooktip(writeFormDTO.getCookTip());
	}


}
