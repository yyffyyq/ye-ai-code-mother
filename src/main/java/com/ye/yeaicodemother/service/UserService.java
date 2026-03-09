package com.ye.yeaicodemother.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.ye.yeaicodemother.model.dto.UserQueryRequest;
import com.ye.yeaicodemother.model.entity.User;
import com.ye.yeaicodemother.model.vo.LoginUserVO;
import com.ye.yeaicodemother.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
public interface UserService extends IService<User> {

    /**
     * 根据查询条件构造数据查询参数
     * @param userQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest);


    /**
     * 用户退出
     * @param req
     * @return
     */
    boolean userLogout(HttpServletRequest req);


    /**
     * 获取当前登录用户
     * @param request
     * @return
     */

    User getLoginUser(HttpServletRequest request);


    /**
     * 获取脱敏后的用户信息
     * @param user 用户信息
     * @return
     */
    UserVO getUserVo(User user);

    /**
     * 获取脱敏后的用户信息(分页)
     * @param userList 用户列表
     * @return
     */
    List<UserVO> getUserVoList(List<User> userList);



    /**
     * 获取脱敏后的用户信息返回回去
     * @param user 未脱敏的用户信息
     * @return 脱敏后的vo返回值
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的已登录用户信息
     * @param userAccount 用户信息
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);



    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 加密
     * @param userPassword 原名密码
     *
     * @return 加密后密码
     */
    String getEncryptPassword(String userPassword);


}
