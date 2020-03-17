package com.joezeo.joefgame.potal.shiro;

import com.joezeo.joefgame.dao.mapper.UserMapper;
import com.joezeo.joefgame.dao.mapper.UserRoleMapper;
import com.joezeo.joefgame.dao.pojo.Role;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.dao.pojo.UserExample;
import com.joezeo.joefgame.potal.dto.UserDTO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class UserShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 进行授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

        UserDTO userDTO = (UserDTO) principals.getPrimaryPrincipal();
        Set<String> roleNames = new HashSet<>();
        // 从数据库查询该email对应用户拥有的角色情况
        List<Role> roleList = userRoleMapper.selectRolesByEmail(userDTO.getEmail());
        roleList.stream().forEach(role -> roleNames.add(role.getName()));

        // 设置权限
        authorizationInfo.addRoles(roleNames);
        return authorizationInfo;
    }

    /**
     * 进行认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String account = (String) token.getPrincipal();
        if("email".equals(account)){
            return null;
        }

        // 以邮箱/密码方式进行登录认证
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(account);
        List<User> users = userMapper.selectByExample(example);
        if(users == null || users.size()!=1){
            return null;
        }
        User user = users.get(0);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);

        // 查询该用户的权限
        List<Role> roles = userRoleMapper.selectRolesById(user.getId());
        List<String> names = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        userDTO.setRoles(names);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                userDTO,
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName());

        return info;
    }
}
