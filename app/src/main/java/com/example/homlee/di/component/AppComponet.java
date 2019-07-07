package com.example.homlee.di.component;

import com.example.homlee.di.module.HttpModule;
import com.example.homlee.http.HttpService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = HttpModule.class)
public interface AppComponet {
    HttpService getHttpService();
}
