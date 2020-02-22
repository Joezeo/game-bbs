package com.joezeo.community.shiro;

import com.joezeo.community.mapper.UserMapper;
import com.joezeo.community.pojo.User;
import com.joezeo.community.pojo.UserExample;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class UserShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 进行授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 进行认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String email = (String) token.getPrincipal();
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(example);
        if(users == null || users.size()!=1){
            return null;
        }
        User user = users.get(0);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(
                user.getEmail(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName());

        return info;
    }
}
