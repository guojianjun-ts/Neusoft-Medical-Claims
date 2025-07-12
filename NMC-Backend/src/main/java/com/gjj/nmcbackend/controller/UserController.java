package com.gjj.nmcbackend.controller;

import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.user.UserLoginRequest;
import com.gjj.nmcbackend.model.dto.user.UserRegisterRequest;
import com.gjj.nmcbackend.model.entity.User;
import com.gjj.nmcbackend.model.vo.LoginUserVO;
import com.gjj.nmcbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null , ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount , userPassword , checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @GetMapping("/get/login")
    // 获取登录用户
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        // 调用userService的getLoginUser方法，获取登录用户
        User user = userService.getLoginUser(request);
        // 调用userService的getLoginUserVO方法，将用户信息转换为LoginUserVO对象
        return ResultUtils.success(userService.getLoginUserVO(user));
    }



}


