package com.refrigerator.springboot.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="tagBigImg")
@Getter
@Setter
@ToString
public class tagBigImg {
    @Id
    @Column(name = "img_big_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String srcs;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="big_id")
    private BigTags bigTags;

}
