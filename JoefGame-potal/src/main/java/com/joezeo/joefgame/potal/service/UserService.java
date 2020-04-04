package com.joezeo.joefgame.potal.service;

import com.joezeo.joefgame.common.dto.PaginationDTO;
import com.joezeo.joefgame.dao.pojo.User;
import com.joezeo.joefgame.common.dto.SteamAppDTO;
import com.joezeo.joefgame.common.dto.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService{
    /**
     * 根据用户邮箱查询用户信息
     * @param email
     * @return userDTO
     */
    UserDTO queryUserByEmail(String email);

    /**
     * 根据用户AccoutId查询用户信息（即GithubAccountID）
     * @param accountId GithubAccountID
     * @return userDTO
     */
    UserDTO queryByAccountid(String accountId);

    /**
     * 根据用户主键id查询用户信息
     * @param userid
     * @return
     */
    User queryByUserid(Long userid);

    /**
     * 用户注册功能
     * @param user 待注册用户信息
     */
    void signup(User user);

    /**
     * Github第三方登录
     * @param githubID
     */
    void loginBaseGithub(String githubID);

    /**
     * Steam第三方登录
     * @param steamid
     */
    void loginBaseSteam(String steamid);

    /**
     * 用户基本登录
     * @param user
     * @param isRemember 是否'记住我'
     */
    void login(User user, boolean isRemember);

    /**
     * 用户注销
     */
    void logout();

    /**
     * 检查传入的email地址是否已被使用
     * @param targetEmail
     * @return 是否被使用 boolean
     */
    boolean checkEmail(String targetEmail);

    /**
     * 检查该GithubID对应的用户是否已经存在
     * @param githubID
     * @return 是否存在
     */
    boolean isExistGithubUser(String githubID);

    /**
     * 检查该SteamID对应的用户是否已经存在
     * @param steamid
     * @return
     */
    boolean isExistSteamUser(String steamid);

    /**
     * 返回目标userid的关注列表
     * @param userid
     * @param page
     * @return 分页后的关注数据
     */
    PaginationDTO<UserDTO> listFollowUser(Long userid, Integer page);

    /**
     * 返回目标userid的Steam收藏列表
     * @param userid
     * @param page
     * @return 返回分页数据
     */
    PaginationDTO<SteamAppDTO> listFavoriteApp(Long userid, Integer page);

    /**
     * 返回目标userid的所有关注用户
     * @param userid
     * @return
     */
    List<UserDTO> listAllFollowUser(Long userid);

    /**
     * 更新用户的头像
     * @param avatar MultipartFile
     * @param userid
     * @param oldAvatarUrl 用户原来的头像地址
     * @return 返回上传的头像的新的url地址
     */
    String updateAvatar(MultipartFile avatar, Long userid, String oldAvatarUrl);

    /**
     * 生成随机头像更新用户的头像
     * @param userid
     * @param oldAvatarUrl 用户原来的头像地址
     * @return 返回上传的头像的新的url地址
     */
    String updateAvatar(Long userid, String oldAvatarUrl);

    /**
     * 修改用户的个性签名
     * @param userDTO user数据传输对象
     */
    void updateBio(UserDTO userDTO);
}
