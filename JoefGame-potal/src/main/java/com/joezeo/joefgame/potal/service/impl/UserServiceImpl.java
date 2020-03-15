package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.dto.GithubUser;
import com.joezeo.joefgame.common.dto.SteamUser;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.common.provider.UCloudProvider;
import com.joezeo.joefgame.common.utils.AvatarGenerator;
import com.joezeo.joefgame.common.utils.PasswordHelper;
import com.joezeo.joefgame.dao.mapper.UserMapper;
import com.joezeo.joefgame.dao.mapper.UserRoleMapper;
import com.joezeo.joefgame.dao.pojo.Role;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.dao.pojo.UserExample;
import com.joezeo.joefgame.potal.dto.UserDTO;
import com.joezeo.joefgame.potal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.wml.WMLEmElement;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UCloudProvider uCloudProvider;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean isExistGithubUser(String githubID) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andGithubAccountIdEqualTo(githubID);
        List<User> memUser = userMapper.selectByExample(userExample);

        return (memUser == null || memUser.size() == 0) ? false : true;
    }

    @Override
    public boolean isExistSteamUser(String steamid) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andSteamIdEqualTo(steamid);
        List<User> memUser = userMapper.selectByExample(userExample);

        return (memUser == null || memUser.size() == 0) ? false : true;
    }

    @Override
    public void signup(User user) {
        passwordHelper.encryptPassword(user); // 密码加密
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModify(user.getGmtCreate());

        // 随机生成头像
        InputStream avatar = new AvatarGenerator().getARandomAvatar();
        String avatarUrl = uCloudProvider.uploadAvatar(avatar, "image/jpeg", "avatar-" + UUID.randomUUID().toString() + ".jpg");
        user.setAvatarUrl(avatarUrl);

        int count = userMapper.insertSelective(user);
        if (count != 1) {
            log.error("函数createOrUpdate：保存新用户失败");
            throw new CustomizeException(CustomizeErrorCode.CREATE_NEW_USER_FAILD);
        }
    }

    @Override
    public void loginBaseGithub(String githubID) {
        // 进行Shiro验证
        // 使用三方登录时默认保存登录状态
        UsernamePasswordToken shiroToken = new UsernamePasswordToken("3-part-login", githubID, true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(shiroToken);
        } catch (ShiroException e) {
            log.error("用户进行Github三方登录失败，失败id:" + githubID);
            throw new CustomizeException(CustomizeErrorCode.USER_GITHUB_LOGIN_FALIED);
        }
    }

    @Override
    public void loginBaseSteam(String steamid) {
        // 进行Shiro验证
        // 使用三方登录时默认保存登录状态
        UsernamePasswordToken shiroToken = new UsernamePasswordToken("3-part-login", steamid, true);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(shiroToken);
        } catch (ShiroException e) {
            log.error("用户进行Steam三方登录失败，失败id:" + steamid);
            throw new CustomizeException(CustomizeErrorCode.USER_STEAM_LOGIN_FALIED);
        }
    }

    @Override
    public void login(User user, boolean isRemember) {
        UsernamePasswordToken shiroToken = new UsernamePasswordToken(user.getEmail(), user.getPassword(), isRemember);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(shiroToken);
        } catch (ShiroException e) {
            log.error("用户登录失败,尝试登录的email：" + user.getEmail());
            throw new CustomizeException(CustomizeErrorCode.USER_LOGIN_FALIED);
        }

    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }


    @Override
    public boolean checkEmail(String targetEmail) {
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(targetEmail);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() != 0) {
            return true;
        }
        return false;
    }


    @Override
    public UserDTO queryUserByEmail(String email) {
        UserDTO userDTO = new UserDTO();

        // 前方已经保证token！=null 或空，不必再做判断
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> user = userMapper.selectByExample(userExample);
        if (user == null || user.size() == 0) {
            return null;
        }
        BeanUtils.copyProperties(user.get(0), userDTO);

        // 查询该用户的权限
        List<Role> roles = userRoleMapper.selectRolesById(userDTO.getId());
        List<String> names = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        userDTO.setRoles(names);

        // 将密码设为null
        userDTO.setPassword(null);

        return userDTO;
    }

    @Override
    public UserDTO queryByAccountid(String accountId) {
        UserDTO userDTO = new UserDTO();
        if (accountId == null || "".equals(accountId)) {
            log.error("函数queryByAccoundid：参数accountId为空");
            throw new ServiceException("参数异常");
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andGithubAccountIdEqualTo(accountId);
        List<User> user = userMapper.selectByExample(userExample);
        if (user == null) {
            log.error("函数queryByAccountid：获取user信息失败，该accounid不存在，可能前端传参有问题");
            throw new ServiceException("获取user失败");
        }

        BeanUtils.copyProperties(user.get(0), userDTO);

        // 查询该用户的权限
        List<Role> roles = userRoleMapper.selectRolesById(userDTO.getId());
        List<String> names = roles.stream().map(role -> role.getName()).collect(Collectors.toList());
        userDTO.setRoles(names);

        // 将密码设置为null
        userDTO.setPassword(null);
        return userDTO;
    }

    @Override
    public User queryByUserid(Long userid) {
        User user = userMapper.selectByPrimaryKey(userid);
        if (user == null) {
            log.error("通过主键获取user失败,userid=" + userid);
            throw new CustomizeException(CustomizeErrorCode.SERVER_ERROR);
        }

        // 将密码、salt设置为null
        user.setPassword(null);
        user.setSalt(null);
        return user;
    }
}
