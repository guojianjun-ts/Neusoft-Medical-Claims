package com.gjj.nmcbackend.model.constant;

public interface UserConstant {

    /**
     * 用户登录态键（Session中存储的用户登录标识）
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 用户状态

    /**
     * 用户状态 - 启用
     */
    Integer USER_STATUS_ENABLED = 0;

    /**
     * 用户状态 - 禁用
     */
    Integer USER_STATUS_DISABLED = 1;

    // endregion
}

