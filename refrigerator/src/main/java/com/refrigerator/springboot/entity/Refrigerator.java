package com.refrigerator.springboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="refrigerator")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Refrigerator {
    @Id
    @Column(name = "ref_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mem_id")
    private Member member;

    public Refrigerator(Member member){
        this.member = member;
    }

}
