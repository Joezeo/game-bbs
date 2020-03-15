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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ThreePartyLoginShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 授权
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
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 以Github三方登录方式进行登录验证
        // 走一下表面过场就行，直接验证通过
        String account = (String) token.getPrincipal();
        if(!"3-part-login".equals(account)){
            return null;
        }

        String githubAccoutId = String.valueOf((char[]) token.getCredentials());
        UserExample example = new UserExample();
        example.createCriteria().andGithubAccountIdEqualTo(githubAccoutId);
        User user = userMapper.selectByExample(example).get(0);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        // 查询该用户的权限
        List<Role> roles = userRoleMapper.selectRolesById(user.getId());
        List<String> names = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        userDTO.setRoles(names);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDTO, githubAccoutId, getName());
        return info;
    }
}
