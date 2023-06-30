package com.chat.security;

import com.chat.entity.User;
import com.chat.service.impl.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Component
public class FaceRecognitionAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    FaceRecognitionService faceRecognitionService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FaceRecognitionAuthenticationToken authenticationToken = (FaceRecognitionAuthenticationToken) authentication;
        // 这里应该是你的人脸识别的图片信息
        Object principal = authenticationToken.getPrincipal();
        // 在这里，你可以使用你的FaceRecognitionService来认证用户
        UserDetails user = faceRecognitionService.recognize((String) principal);
        if (user == null) {
            return new FaceRecognitionAuthenticationToken(null, null);
        }
        FaceRecognitionAuthenticationToken authenticatedToken =
                new FaceRecognitionAuthenticationToken(user, null);
        return authenticatedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return FaceRecognitionAuthenticationToken.class.equals(authentication);
    }
}