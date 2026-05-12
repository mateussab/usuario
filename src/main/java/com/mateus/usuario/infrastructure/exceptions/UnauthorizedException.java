package com.mateus.usuario.infrastructure.exceptions;


import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String mensage){
        super(mensage);
    }

    public UnauthorizedException(String mensage, Throwable cause){
        super(mensage,cause);
    }
}
