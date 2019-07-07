package com.example.homlee.http;

import com.example.homlee.http.bean.response.SimpleResultData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * @author leo.yuan
 */
public interface HttpService {
    @GET("account")
    Observable<SimpleResultData> login(@Query("account") String account, @Query("password") String pwd);
}
