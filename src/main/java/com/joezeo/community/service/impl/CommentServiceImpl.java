package com.joezeo.community.service.impl;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.*;
import com.joezeo.community.pojo.*;
import com.joezeo.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addComment(CommentDTO commentDTO, Long userid) {
        if (commentDTO == null) {
            throw new ServiceException("参数commentDTO异常，=null");
        }
        if (userid == null || userid <= 0) {
            throw new ServiceException("参数userid异常");
        }

        if (commentDTO.getParentId() == null || commentDTO.getParentId() <= 0) {
            throw new CustomizeException(CustomizeErrorCode.PARENT_ID_NOT_TRANSFER);
        }

        if (commentDTO.getParentType() == null || !CommentTypeEnum.isExist(commentDTO.getParentType())) {
            throw new CustomizeException(CustomizeErrorCode.PARENT_TYPR_ILEEAGUE);
        }

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setUserid(userid);
        comment.setLikeCount(0);
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(comment.getGmtCreate());
        comment.setCommentCount(0);

        // 判断当前评论是评论问题，还是回复评论的
        if(comment.getParentType() == CommentTypeEnum.QUESTION.getType()){ // 回复问题
            Question question = questionMapper.selectByPrimaryKey(commentDTO.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            // 累加评论数
            question.setCommentCount(1);
            int count = questionExtMapper.incComment(question);
            if(count != 1){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
            }
        } else { // 回复评论
            Comment memComment = commentMapper.selectByPrimaryKey(commentDTO.getParentId());
            if(memComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 累加二级评论数
            memComment.setCommentCount(1);
            int count = commentExtMapper.incComment(memComment);
            if(count != 1){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
            }
        }

        int count = commentMapper.insertSelective(comment);
        if (count != 1) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
        }
    }

    @Override
    public List<CommentDTO> listByParentId(Long parentId, CommentTypeEnum typeEnum) {
        // 查询评论
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(parentId)
                .andParentTypeEqualTo(typeEnum.getType()); // 根据父类型以及父类型id 来查询所有评论
        List<Comment> comments = commentMapper.selectByExample(example);

        // 如果没有评论直接返回一个空的集合
        if(comments == null || comments.size() == 0){
            return new ArrayList<>();
        }

        // 获取所有评论的评论者id，并且去重
        List<Long> userids = comments.stream().map(c -> c.getUserid()).distinct().collect(Collectors.toList());

        // 根据评论者id所有评论者数据，并放入map中
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userids);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 将 Comment 转化为 CommentDto 并且放入user
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getUserid()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
