package com.joezeo.community.cache;

import com.joezeo.community.dto.TagDTO;
import com.joezeo.community.enums.TagCategoryEnum;
import com.joezeo.community.pojo.Tag;
import com.joezeo.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagCache {
    @Autowired
    private TagService tagService;

    public List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();

        // 开发语言
        TagDTO languge = new TagDTO(TagCategoryEnum.LANGUAGE);
        List<Tag> lanList = tagService.listTagsByCategory(TagCategoryEnum.LANGUAGE.getIndex());
        languge.setTags(lanList);
        tagDTOS.add(languge);

        // 平台框架
        TagDTO framework = new TagDTO(TagCategoryEnum.FRAMEWORK);
        List<Tag> fraList = tagService.listTagsByCategory(TagCategoryEnum.FRAMEWORK.getIndex());
        framework.setTags(fraList);
        tagDTOS.add(framework);

        // 服务器
        TagDTO server = new TagDTO(TagCategoryEnum.SERVER);
        List<Tag> serList = tagService.listTagsByCategory(TagCategoryEnum.SERVER.getIndex());
        server.setTags(serList);
        tagDTOS.add(server);

        // 数据库
        TagDTO db = new TagDTO(TagCategoryEnum.DATABASE);
        List<Tag> dbList = tagService.listTagsByCategory(TagCategoryEnum.DATABASE.getIndex());
        db.setTags(dbList);
        tagDTOS.add(db);

        // 开发工具
        TagDTO tool = new TagDTO(TagCategoryEnum.TOOL);
        List<Tag> toolList = tagService.listTagsByCategory(TagCategoryEnum.TOOL.getIndex());
        tool.setTags(toolList);
        tagDTOS.add(tool);

        return tagDTOS;
    }

    /**
     * 判断标签是否存在非法项
     * 如果不存在返回一个空的list
     * 如存在则返回非法项的list
     */
    public List<String> check(String tags){
        List<TagDTO> tagDTOS = get();
        // 通过流取出所有Tag的name，生成List
        List<String> tagNames = tagDTOS.stream().flatMap(tagDTO -> tagDTO.getTags().stream())
                .map(tag -> tag.getName())
                .collect(Collectors.toList());

        // 找出tagNames中没有的非法项，生成list
        List<String> tagList = Arrays.stream(tags.split(","))
                .filter(tag -> !tagNames.contains(tag))
                .collect(Collectors.toList());
        return tagList;
    }
}
