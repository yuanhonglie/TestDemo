package com.example.homlee.di.module;

import com.example.homlee.http.HttpService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author leo.yuan
 */
@Module
public class HttpModule {
    private static final String BASE_URL = "www.leoyuan.com";

    @Provides
    @Singleton
    OkHttpClient provideHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Retrofit.Builder builder, OkHttpClient client) {
        return builder.baseUrl(BASE_URL).client(client).build();
    }

    @Provides
    @Singleton
    HttpService provideHttpService(Retrofit retrofit) {
        return retrofit.create(HttpService.class);
    }
}
