package com.refrigerator.springboot.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ingredient")
public class Ingredient {
	@Id
	@Column(name = "ing_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;



	private LocalDateTime ing_deadline;

	@OneToOne()
	@JoinColumn(name = "small_id")
	private SmallTags smallTag;



	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ref_id")
	private Refrigerator refrigerator;


	private LocalDateTime reg_time;

	private String textareas;
	public static Ingredient create(SmallTags smallTag,Refrigerator  refrigerator,LocalDateTime ing_deadline,String textareases){
		Ingredient ingeredient = new Ingredient();
		ingeredient.smallTag = smallTag;
		ingeredient.refrigerator = refrigerator;
		ingeredient.ing_deadline  = ing_deadline;
		ingeredient.reg_time = LocalDateTime.now();
		ingeredient.textareas = textareases;
		return ingeredient;
	}
}
