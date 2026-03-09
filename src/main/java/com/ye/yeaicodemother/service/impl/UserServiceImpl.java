package com.ye.yeaicodemother.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.ye.yeaicodemother.exception.BusinessException;
import com.ye.yeaicodemother.exception.ErrorCode;
import com.ye.yeaicodemother.model.dto.UserQueryRequest;
import com.ye.yeaicodemother.model.entity.User;
import com.ye.yeaicodemother.mapper.UserMapper;
import com.ye.yeaicodemother.model.enums.UserRoleEnum;
import com.ye.yeaicodemother.model.vo.LoginUserVO;
import com.ye.yeaicodemother.model.vo.UserVO;
import com.ye.yeaicodemother.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ye.yeaicodemother.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author <a href="https://github.com/yyffyyq">代码制造者yfy</a>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{
    
    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        return QueryWrapper.create()
                .eq("id", id) // = where  id = ${id}
                .eq("userRole", userRole) // = and userRole = ${userRole}
                .like("userAccount", userAccount)
                .like("userName", userName)
                .like("userProfile", userProfile)
                .orderBy(sortField, "ascend".equals(sortOrder));
    }


    @Override
    public UserVO getUserVo(User user) {
        //判断参数是否为空
        if(user==null){
            return null;
        }
//        创建封装对象
        UserVO userVO = new UserVO();
        //封装
        BeanUtils.copyProperties(user,userVO);
        //返回vo值
        return userVO;
    }

    @Override
    public List<UserVO> getUserVoList(List<User> userList) {
        if(CollUtil.isEmpty(userList)){
            return new ArrayList<>();
        }
        //landa表达式
        return userList.stream()
                .map(this::getUserVo)
                .collect(Collectors.toList());
    }




    @Override
    public boolean userLogout(HttpServletRequest req) {
//        获取用户信息
        Object userObj = req.getSession().getAttribute(USER_LOGIN_STATE);
//        判断用户是否登录
        if(userObj == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"用户未登录");
        }
        //移除登录态
        req.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        //判断用户是否登录
        //获取到用户信息
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null || currentUser.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //根据用户信息查询数据库
        long UserId = currentUser.getId();
        currentUser = this.getById(UserId);
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //返回这个用户信息
        return currentUser;
    }



    @Override
    public LoginUserVO getLoginUserVO(User user) {
        //先判断传来的值是否为空
        if(user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //将用户账号信息封装vo
        LoginUserVO loginUserVO = new LoginUserVO();

        BeanUtils.copyProperties(user,loginUserVO);

        //返回值
        return loginUserVO;
    }


    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //判断发送过来参数是否为空
        if (StrUtil.hasBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度过断，要大于4位");
        }
        if(userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过断");
        }
        //对用户传入的密码进行加密
        String encryptPassword = getEncryptPassword(userPassword);
        //查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        //判断密码是否正确
        User user = this.mapper.selectOneByQuery(queryWrapper);
        if(user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在或者密码错误");
        }
        //如果用户存在，更新用户的登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return this.getLoginUserVO(user);
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验参数
        if(StrUtil.hasBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度过断，要大于4位");
        }
        if(userPassword.length()<8|| checkPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过断");
        }
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        //2.查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.mapper.selectCountByQuery(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号存在");
        }
        //3.加密密码
        String encryptPassword = getEncryptPassword(userPassword);
        //4.创建用户，插入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("默认用户名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"保存失败，数据库错误");
        }
        System.out.println("2");
        return user.getId();
    }

    @Override
    public String getEncryptPassword(String userPassword) {
        //盐值，混淆密码
        final String SALT = "yfy";
        return DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes(StandardCharsets.UTF_8));
    }
}
