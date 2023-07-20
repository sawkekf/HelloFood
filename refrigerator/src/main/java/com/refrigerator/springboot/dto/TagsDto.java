package com.refrigerator.springboot.dto;

import com.refrigerator.springboot.entity.SmallTags;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class TagsDto {
    private SmallTags small_tag;

    private List<TagsDto> smalltaglist;
}
