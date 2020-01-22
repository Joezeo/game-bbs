package com.joezeo.community.service.impl;

import com.joezeo.community.dto.CommentDTO;
import com.joezeo.community.enums.CommentTypeEnum;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.CommentMapper;
import com.joezeo.community.mapper.QuestionExtMapper;
import com.joezeo.community.mapper.QuestionMapper;
import com.joezeo.community.pojo.Comment;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

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

        // 判断当前评论是评论问题，还是回复评论的
        if(comment.getParentType() == CommentTypeEnum.QUESTION.getType()){
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
        } else {
            Comment memCommet = commentMapper.selectByPrimaryKey(commentDTO.getParentId());
            if(memCommet == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
        }

        int count = commentMapper.insertSelective(comment);
        if (count != 1) {
            throw new CustomizeException(CustomizeErrorCode.COMMENT_FAILD);
        }
    }
}
