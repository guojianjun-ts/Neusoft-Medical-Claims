package com.gjj.nmcbackend.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAuthCheck {

    /**
     * 是否需要登录校验，默认需要
     */
    boolean checkLogin() default true;

    /**
     * 是否需要用户状态校验，默认需要
     */
    boolean checkStatus() default true;
}
