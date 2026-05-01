package com.mateus.usuario.infrastructure.exceptions;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensage){
        super(mensage);
    }

    public ResourceNotFoundException(String mensage, Throwable cause){
        super(mensage,cause);
    }
}
