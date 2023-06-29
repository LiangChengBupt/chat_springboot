package com.chat.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class FaceRecognitionAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    public FaceRecognitionAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}