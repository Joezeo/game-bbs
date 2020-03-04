package com.joezeo.joefgame.dao.mapper;

import com.joezeo.joefgame.dao.pojo.Role;

import java.util.List;

public interface UserRoleMapper {
    List<Role> selectRolesByEmail(String email);

    List<Role> selectRolesById(Long userid);
}
