package com.refrigerator.springboot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "member_img")
@Setter @Getter
public class MemberImg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ImgId;

    @ManyToOne
    @JoinColumn(name = "mem_id")
    private Member member;

    @Column(name = "oldImg")
    private String oldImg;

    @Column(name = "newImg")
    private String newImg;

    @Column(name = "img_url")
    private String imgUrl;

    public void updateMemImg(String oldImg, String newImg, String imgUrl){
        this.oldImg=oldImg;
        this.newImg=newImg;
        this.imgUrl=imgUrl;
    }
}
