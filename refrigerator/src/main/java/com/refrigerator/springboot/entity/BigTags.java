package com.refrigerator.springboot.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Getter
@Setter
@ToString
@Table(name = "bigtag")
public class BigTags {
    @Id
    @Column(name = "big_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String big_name;


}
