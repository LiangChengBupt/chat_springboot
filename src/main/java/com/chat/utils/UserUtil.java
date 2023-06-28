package com.chat.utils;

import com.chat.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author gzx
 * 用户工具类
 */
public class UserUtil {
    /**
     * 获取当前登录用户实体
     * @return
     */
    public static User getCurrentUser(){
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}