package com.refrigerator.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Selectdto {

    private String text ;

    private LocalDateTime datae ;



    private String memo ;


}
