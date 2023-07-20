package com.refrigerator.springboot.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="tagImg")
@Getter
@Setter
@ToString
public class tagImg {
    @Id
    @Column(name = "img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String srcs;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="small_id")
    private SmallTags smallTags;

}
