package com.joezeo.joefgame.dto;

import com.joezeo.joefgame.enums.TagCategoryEnum;
import com.joezeo.joefgame.pojo.Tag;
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
