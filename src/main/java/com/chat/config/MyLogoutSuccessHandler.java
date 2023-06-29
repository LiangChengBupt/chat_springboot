package com.chat.config;

import com.chat.entity.RespBean;
import com.chat.entity.User;
import com.chat.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author gzx
 */
@Configuration
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Override

    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
        //更新用户状态为离线
        User user = (User) authentication.getPrincipal();
        userService.setUserStateToLeave(user.getId());
        //广播系统消息
        simpMessagingTemplate.convertAndSend("/topic/notification", "系统消息：用户【" + user.getNickname() + "】退出了聊天室");
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("成功退出！")));
        out.flush();
        out.close();
    }
}
