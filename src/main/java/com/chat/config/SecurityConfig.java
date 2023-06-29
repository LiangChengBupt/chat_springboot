package com.chat.config;

import com.chat.security.*;
import com.chat.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author gzx
 */

@EnableWebSecurity
public class SecurityConfig {
    //密码加密方案
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Configuration
    @Order(1)
    public static class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        UserServiceImpl userService;
        @Autowired
        VerificationCodeFilter verificationCodeFilter;
        @Autowired
        SimpMessagingTemplate simpMessagingTemplate;
        @Autowired
        MyAuthenticationFailureHandler myAuthenticationFailureHandler;
        @Autowired
        MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
        @Autowired
        MyLogoutSuccessHandler myLogoutSuccessHandler;
        @Autowired
        private SessionRegistry sessionRegistry;

        //验证服务
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService);
        }

        //        //忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/login", "/verifyCode", "/file", "/user/register", "/user/personImg/**", "/user/checkUsername", "/user/checkNickname");
        }

        //登录验证
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //将验证码过滤器添加在用户名密码过滤器的前面
            http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
            http.sessionManagement()
                    .maximumSessions(1)
                    .sessionRegistry(sessionRegistry)
                    .and()
                    .invalidSessionUrl("/login?expired")
                    .and()
                    .sessionManagement()
                    .invalidSessionStrategy(new InvalidSessionStrategy() {
                        @Override
                        public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json;charset=UTF-8");
                            PrintWriter out = response.getWriter();
                            out.write("{\"error\":\"Session expired\"}");
                            out.flush();
                            out.close();
                        }
                    })
                    .and()
                    .authorizeRequests(authorizeRequests ->
                            authorizeRequests
                                    .antMatchers("/doLogin").authenticated()
                    )
                    .formLogin()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginPage("/login")
                    .loginProcessingUrl("/doLogin")
                    //成功处理
                    .successHandler(myAuthenticationSuccessHandler)
                    //失败处理
                    .failureHandler(myAuthenticationFailureHandler)
                    .permitAll()//返回值直接返回
                    .and()
                    //登出处理
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(myLogoutSuccessHandler)
                    .permitAll()
                    .and()
                    .csrf().disable()//关闭csrf防御方便调试
                    //没有认证时，在这里处理结果，不进行重定向到login页
                    .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                            httpServletResponse.setStatus(401);
                        }
                    });
        }
    }

    @Configuration
    @Order(2)
    public static class FaceRecognitionSecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        UserServiceImpl userService;
        @Autowired
        FaceRecognitionAuthenticationProvider faceRecognitionAuthenticationProvider;
        @Autowired
        FaceRecognitionFilter faceRecognitionFilter;
        @Autowired
        SimpMessagingTemplate simpMessagingTemplate;
        @Autowired
        MyAuthenticationFailureHandler myAuthenticationFailureHandler;
        @Autowired
        MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
        @Autowired
        MyLogoutSuccessHandler myLogoutSuccessHandler;

        //验证服务
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService);
        }


        //        忽略"/login","/verifyCode"请求，该请求不需要进入Security的拦截器
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/login", "/verifyCode", "/file", "/user/personImg/**", "/user/register", "/user/checkUsername", "/user/checkNickname");
        }

        //登录验证
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //将验证码过滤器添加在用户名密码过滤器的前面
            http.addFilterBefore(faceRecognitionFilter, UsernamePasswordAuthenticationFilter.class);
            http.authorizeRequests(authorizeRequests ->
                            authorizeRequests
                                    .antMatchers("/doLoginWithFace").authenticated()
                    )
                    .formLogin()
                    .loginProcessingUrl("/doLoginWithFace")
                    //成功处理
                    .successHandler(myAuthenticationSuccessHandler)
                    //失败处理
                    .failureHandler(myAuthenticationFailureHandler)
                    .permitAll()//返回值直接返回
                    .and()
                    //登出处理
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(myLogoutSuccessHandler)
                    .permitAll()
                    .and()
                    .csrf().disable()//关闭csrf防御方便调试
                    //没有认证时，在这里处理结果，不进行重定向到login页
                    .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                            httpServletResponse.setStatus(401);
                        }
                    });
        }
    }
}