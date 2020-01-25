package com.joezeo.community.dto;

import com.joezeo.community.enums.TagCategoryEnum;
import com.joezeo.community.pojo.Tag;
import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String category;
    private List<Tag> tags;

    public TagDTO(TagCategoryEnum categoryEnum){
        this.category = categoryEnum.getCategory();
    }
}
