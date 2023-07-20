package com.refrigerator.springboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "smalltag")
@SequenceGenerator(
        name="USER_SEQ_GEN", //시퀀스 제너레이터 이름
        sequenceName="USER_SEQ", //시퀀스 이름
        initialValue=1500, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)
@NoArgsConstructor
public class SmallTags {
    @Id
    @Column(name = "small_id")
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE, //사용할 전략을 시퀀스로  선택
            generator="USER_SEQ_GEN" //식별자 생성기를 설정해놓은  USER_SEQ_GEN으로 설정
    )
    private Long id;

    private String tag_small_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "big_id")
    private BigTags bigTags;

    public SmallTags(String name,BigTags bigTags){
        this.tag_small_name = name;
        this.bigTags = bigTags;
    }
}