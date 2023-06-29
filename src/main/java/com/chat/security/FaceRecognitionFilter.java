package com.chat.security;

import com.chat.config.MyAuthenticationSuccessHandler;
import com.chat.entity.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class FaceRecognitionFilter extends GenericFilter {

    @Autowired
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    FaceRecognitionAuthenticationProvider faceRecognitionAuthenticationProvider;

    @Autowired
    SessionRegistry sessionRegistry;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if ("/doLoginWithFace".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            try {
                System.out.println("进入人脸识别");
                String faceImageData = request.getParameter("personImg").replace(" ", "+");
//                System.out.println(faceImageData);
                if (faceImageData.equals("")) {
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(RespBean.error("请给出人体面部图像！")));
                    writer.flush();
                    writer.close();
                    return;
                }
//                System.out.println(faceImageData);
                FaceRecognitionAuthenticationToken authRequest = new FaceRecognitionAuthenticationToken(faceImageData, null);

                Authentication authResult = faceRecognitionAuthenticationProvider.authenticate(authRequest);
//                System.out.println(authResult.getPrincipal());
                if (authResult != null) {
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                    sessionRegistry.registerNewSession(request.getRequestedSessionId(), authResult.getPrincipal());
                    myAuthenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
                    // 不再调用后续过滤器，直接返回
                } else {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(RespBean.error("人员匹配失败！")));
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(RespBean.error("人脸识别异常！")));
                writer.flush();
                writer.close();
            }
        }
        else
            filterChain.doFilter(request, response);
    }
}