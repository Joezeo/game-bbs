package com.joezeo.joefgame.potal.dto;

import com.joezeo.joefgame.common.enums.TagCategoryEnum;
import com.joezeo.joefgame.dao.pojo.Tag;
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
