package com.example.oauth.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

public class MyCustomRuntimeException extends RuntimeException{
    public MyCustomRuntimeException(HttpStatusCode httpStatusCode, HttpHeaders httpHeaders){

    }
}
