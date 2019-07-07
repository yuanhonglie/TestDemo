package com.example.homlee.http.bean.response;

/**
 * @author leo.yuan
 */
public class ResultData<T> {
    String errorCode;
    String errorMessage;
    T data;
}
