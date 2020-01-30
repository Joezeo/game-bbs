package com.joezeo.community.service.impl;

import com.joezeo.community.dto.PaginationDTO;
import com.joezeo.community.dto.QuestionDTO;
import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.QuestionExtMapper;
import com.joezeo.community.mapper.QuestionMapper;
import com.joezeo.community.mapper.UserMapper;
import com.joezeo.community.pojo.Question;
import com.joezeo.community.pojo.QuestionExample;
import com.joezeo.community.pojo.User;
import com.joezeo.community.service.QuestionService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<QuestionDTO> list() {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andIdIsNotNull();
        List<Question> questions = questionMapper.selectByExample(questionExample);
        if (questions == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        List<QuestionDTO> list = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getUserid());
            if (user == null) {
                throw new ServiceException("获取用户失败");
            }

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            list.add(questionDTO);
        }

        return list;
    }

    @Override
    public PaginationDTO listPage(Integer page, Integer size, String condition) {
        // 判断搜索条件是否为空
        if (condition != null && !"".equals(condition)) {
            String[] conds = condition.split(" ");
            condition = Arrays.stream(conds).collect(Collectors.joining("|"));
        }

        int count = questionExtMapper.countSearch(condition);

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(page, size, count);

        // 防止页码大于总页数 或者小于1
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

//        QuestionExample questionExample = new QuestionExample();
//        questionExample.setOrderByClause("gmt_create desc");
//        RowBounds rowBounds = new RowBounds(index, size);
//        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
        List<Question> questions = questionExtMapper.selectSearch(index, size, condition);
        if (questions == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        List<QuestionDTO> list = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getUserid());
            if (user == null) {
                throw new ServiceException("获取用户失败");
            }

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            list.add(questionDTO);
        }
        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    @Override
    public PaginationDTO listPage(Long userid, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andUseridEqualTo(userid);
        int count = (int) questionMapper.countByExample(questionExample);

        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setPagination(page, size, count);

        // 防止页码大于总页数 或者小于1
        page = paginationDTO.getPage();
        int index = (page - 1) * size;

        RowBounds rowBounds = new RowBounds(index, size);
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
        if (questions == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }

        List<QuestionDTO> list = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getUserid());
            if (user == null) {
                throw new ServiceException("获取用户失败");
            }

            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            list.add(questionDTO);
        }
        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    @Override
    public QuestionDTO queryById(Long id) {
        if (id == null || id <= 0) {
            throw new ServiceException("QuestionService-queryById 参数id异常");
        }

        // 获取指定id问题
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);

        // 获取当前问题的提问人
        User user = userMapper.selectByPrimaryKey(question.getUserid());
        if (user == null) {
            throw new ServiceException("获取用户数据失败");
        }
        questionDTO.setUser(user);

        // 获取当前问题的相关问题
        String tagRegex = questionDTO.getTag().replaceAll(",", "|");
        List<Question> related = questionExtMapper.selectRelated(questionDTO.getId(), tagRegex);
        if (related == null || related.size() == 0) {
            // 如果没有获取到相关问题直接设置一个空集合
            questionDTO.setRelateds(new ArrayList<>());
        }
        questionDTO.setRelateds(related);

        return questionDTO;
    }

    @Override
    public void createOrUpdate(Question question) {
        if (question == null) {
            throw new RuntimeException("参数question不可为null");
        }

        if (question.getId() == null) { // 进行新增问题操作
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModify(question.getGmtCreate());

            int count = questionMapper.insert(question);
            if (count != 1) {
                throw new RuntimeException("发布新问题失败");
            }
        } else { // 进行更新问题操作
            question.setGmtModify(System.currentTimeMillis());

            int count = questionMapper.updateByPrimaryKeySelective(question);
            if (count != 1) {
                throw new RuntimeException("编辑问题失败");
            }
        }

    }

    @Override
    public void incVie(Long id) {
        if (id == null || id <= 0) {
            throw new ServiceException("传入id值异常");
        }
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);

        int count = questionExtMapper.incView(question);
        if (count != 1) {
            throw new ServiceException("累加阅读数失败");
        }
    }
}
