package com.chat.config;

import com.chat.entity.RespBean;
import com.chat.entity.User;
import com.chat.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Configuration
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
        resp.setContentType("application/json;charset=utf-8");
        PrintWriter out = resp.getWriter();
        User presentUser = (User) authentication.getPrincipal();
        presentUser.setPassword(null);
        String username = presentUser.getUsername();
        // 检测会话是否重复，重复的话就把之前的删掉
        String currentSessionId = req.getSession().getId();
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        if(allPrincipals != null) {
            for (Object principal : allPrincipals) {
                if (!(principal instanceof User))
                    continue;
                User user = (User) principal;
                if (!user.getUsername().equals(username))
                    continue;
                // 获取该用户的所有活动会话
                List<SessionInformation> userSessions = sessionRegistry.getAllSessions(user, false);
                for (SessionInformation session : userSessions) {
                    if (!session.getSessionId().equals(currentSessionId)) {
                        session.expireNow();
//                        sessionRegistry.removeSessionInformation(session.getSessionId());
//                        allPrincipals.remove(principal);
                        simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/errors", "{ \"type\": \"session_expired\" }");

                    }
                }
            }
        }
        //更新用户状态为在线
        userService.setUserStateToOn(presentUser.getId());
        presentUser.setUserStateId(1);
        //广播系统通知消息
        simpMessagingTemplate.convertAndSend("/topic/notification", "系统消息：用户【" + presentUser.getNickname() + "】进入了聊天室");
        RespBean ok = RespBean.ok("登录成功", presentUser);
        String s = new ObjectMapper().writeValueAsString(ok);
        out.write(s);
        out.flush();
        out.close();
    }
}