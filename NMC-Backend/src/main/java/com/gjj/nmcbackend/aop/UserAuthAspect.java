package com.gjj.nmcbackend.aop;

import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.gjj.nmcbackend.model.constant.UserConstant.USER_LOGIN_STATE;

@Aspect
@Component
public class UserAuthAspect {

    @Around("@annotation(userAuthCheck)")
    public Object around(ProceedingJoinPoint joinPoint, UserAuthCheck userAuthCheck) throws Throwable {
        // 1. 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ThrowUtils.throwIf(attributes == null, ErrorCode.PARAMS_ERROR,"无法获取当前请求");
        HttpServletRequest request = attributes.getRequest();

        // 2. 检查登录状态
        if (userAuthCheck.checkLogin()) {
            User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            ThrowUtils.throwIf(currentUser == null, ErrorCode.NOT_LOGIN_ERROR);

            // 3. 检查用户状态
            if (userAuthCheck.checkStatus()) {
                ThrowUtils.throwIf(currentUser.getUserStatus() == 1, ErrorCode.FORBIDDEN_ERROR);

                // 检查逻辑删除状态
                ThrowUtils.throwIf(currentUser.getIsDeleted() == 1, ErrorCode.NO_AUTH_ERROR,"用户被删除");
            }
        }

        // 4. 执行目标方法
        return joinPoint.proceed();
    }
}
