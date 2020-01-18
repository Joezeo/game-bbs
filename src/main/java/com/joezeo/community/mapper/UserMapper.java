package com.joezeo.community.mapper;

import com.joezeo.community.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    int insert(User user);

    User selectByToken(String token);

    User selectByAccountid(String accountId);

    int updateByIdSelective(User user);

    User selectById(Integer userid);
}
