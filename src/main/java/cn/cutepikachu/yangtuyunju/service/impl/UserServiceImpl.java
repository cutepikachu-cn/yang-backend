package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.exception.BusinessException;
import cn.cutepikachu.yangtuyunju.mapper.UserMapper;
import cn.cutepikachu.yangtuyunju.model.dto.user.UserQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.enums.SortOrder;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.LoginUserVO;
import cn.cutepikachu.yangtuyunju.model.vo.UserVO;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.cutepikachu.yangtuyunju.constant.CommonConstant.SALT;
import static cn.cutepikachu.yangtuyunju.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 28944
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-01-25 16:49:37
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String userNickname, String userRole) {
        synchronized (userAccount.intern()) {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
            long count = count(lambdaQueryWrapper);
            if (count > 0) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "账户已存在");
            }
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setUserNickname(userNickname);
            user.setUserRole(userRole.toLowerCase());
            boolean saveResult = save(user);
            if (!saveResult) {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR, "注册失败");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        lambdaQueryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = getOne(lambdaQueryWrapper);
        if (user == null) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "账号或密码错误");
        }
        setUserLoginState(request, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User currentUser = getUserLoginState(request);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        long userId = currentUser.getId();
        currentUser = getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ResponseCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User currentUser = getUserLoginState(request);
        return isAdmin(currentUser);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRole.ADMIN == UserRole.getEnumByValue(user.getUserRole());
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (getUserLoginState(request) == null) {
            throw new BusinessException(ResponseCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public User getUserLoginState(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER_LOGIN_STATE);
    }

    @Override
    public LambdaQueryWrapper<User> getLambdaQueryWrapper(UserQueryRequest userQueryRequest) {

        Long id = userQueryRequest.getId();
        String userNickname = userQueryRequest.getUserNickname();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();

        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(id != null, User::getId, id);
        lambdaQueryWrapper.eq(StringUtils.isNotBlank(userRole), User::getUserRole, userRole);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(userNickname), User::getUserNickname, userNickname);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(userProfile), User::getUserProfile, userProfile);
        // 排序
        // 仅 createTime 和 updateTime
        if (StringUtils.isNotBlank(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, User::getCreateTime);
                case "updatetime" -> lambdaQueryWrapper.orderBy(true, isAsc, User::getUpdateTime);
            }
        }

        return lambdaQueryWrapper;
    }

    @Override
    public void setUserLoginState(HttpServletRequest request, User user) {
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
    }
}




