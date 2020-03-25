package com.joezeo.joefgame.potal.service.impl;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.common.enums.CustomizeErrorCode;
import com.joezeo.joefgame.common.enums.SolrCoreNameEnum;
import com.joezeo.joefgame.common.enums.SteamAppTypeEnum;
import com.joezeo.joefgame.common.exception.CustomizeException;
import com.joezeo.joefgame.common.exception.ServiceException;
import com.joezeo.joefgame.common.provider.UCloudProvider;
import com.joezeo.joefgame.common.utils.AvatarGenerator;
import com.joezeo.joefgame.common.utils.PasswordHelper;
import com.joezeo.joefgame.dao.mapper.*;
import com.joezeo.joefgame.dao.pojo.*;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.UserDTO;
import com.joezeo.joefgame.potal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private UserFavoriteAppMapper userFavoriteAppMapper;
    @Autowired
    private SteamAppInfoMapper steamAppInfoMapper;
    @Autowired
    private PasswordHelper passwordHelper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private SolrClient solrClient;

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

        // 设置初始个性签名
        user.setBio("这个人很神秘，什么都没写。");

        int count = userMapper.insertSelective(user);
        if (count != 1) {
            log.error("函数createOrUpdate：保存新用户失败");
            throw new CustomizeException(CustomizeErrorCode.CREATE_NEW_USER_FAILD);
        }

        /*
        注册成功将用户信息保存至Solr中
         */
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setUserid(user.getId() + "");

        String coreName = SolrCoreNameEnum.USER.getName();
        try {
            solrClient.addBean(coreName, userDTO);
            solrClient.commit(coreName);
        } catch (IOException e) {
            log.error("新增Solr数据失败：[core name:" + coreName + "]" +
                    "[steamApp:+" + userDTO.toString() + "+]");
            log.error("StackTrace:" + e.getStackTrace());
        } catch (SolrServerException e) {
            log.error("新增Solr数据失败：[core name:" + coreName + "]" +
                    "[steamApp:+" + userDTO.toString() + "+]");
            log.error("StackTrace:" + e.getStackTrace());
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

    @Override
    public PaginationDTO<UserDTO> listFollowUser(Long userid, Integer page) {
        PaginationDTO<UserDTO> paginationDTO = new PaginationDTO();

        // 查询总数据条数
        UserFollowExample example = new UserFollowExample();
        example.createCriteria().andUseridEqualTo(userid);
        int count = (int) userFollowMapper.countByExample(example);

        paginationDTO.setPagination(page, 5, count); // 每页展示5个关注的用户
        page = paginationDTO.getPage(); // 防止page参数不合法
        int index = (page - 1) * 5;

        List<UserFollow> userFollows = userFollowMapper.selectByExampleWithRowbounds(example, new RowBounds(index, 5));

        List<UserDTO> list = new ArrayList<>();
        userFollows.stream().forEach(item -> {
            User user = userMapper.selectByPrimaryKey(item.getId());
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword(null);
            list.add(userDTO);
        });

        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    @Override
    public PaginationDTO<SteamAppDTO> listFavoriteApp(Long userid, Integer page) {
        PaginationDTO<SteamAppDTO> paginationDTO = new PaginationDTO<>();

        // 查询总数据条数
        UserFavoriteAppExample example = new UserFavoriteAppExample();
        example.createCriteria().andUseridEqualTo(userid);
        int count = (int) userFavoriteAppMapper.countByExample(example);

        paginationDTO.setPagination(page, 5, count); // 每页展示5个关注的Steam应用
        page = paginationDTO.getPage(); // 防止page参数不合法

        int index = (page - 1) * 5;

        List<UserFavoriteApp> favoriteApps = userFavoriteAppMapper.selectByExampleWithRowbounds(example, new RowBounds(index, 5));

        List<SteamAppDTO> list = new ArrayList<>();
        favoriteApps.stream().forEach(favorite -> {
            SteamAppInfo steamAppInfo = steamAppInfoMapper.selectByAppid(favorite.getAppid(), SteamAppTypeEnum.typeOf(favorite.getType()));
            SteamAppDTO steamAppDTO = new SteamAppDTO();
            BeanUtils.copyProperties(steamAppInfo, steamAppDTO);
            list.add(steamAppDTO);
        });

        paginationDTO.setDatas(list);

        return paginationDTO;
    }

    @Override
    public List<UserDTO> listAllFollowUser(Long userid) {
        UserFollowExample example = new UserFollowExample();
        List<UserFollow> userFollows = userFollowMapper.selectByExample(example);

        List<UserDTO> list = new ArrayList<>();
        userFollows.stream().forEach(item -> {
            User user = userMapper.selectByPrimaryKey(item.getId());
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            userDTO.setPassword(null);
            list.add(userDTO);
        });
        return list;
    }
}
