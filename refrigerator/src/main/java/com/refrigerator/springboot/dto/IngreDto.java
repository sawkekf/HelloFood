package com.refrigerator.springboot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class IngreDto {
    private Long id;

    private LocalDateTime ing_deadline;

    private String smalltags;

    private int ing_amount;


}
