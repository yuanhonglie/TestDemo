package com.example.homlee.activities;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homlee.rxjava.ActivityEvent;
import com.homlee.mvp.base.BaseMvpActivity;
import com.homlee.mvp.base.BasePresenter;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.subjects.BehaviorSubject;

public class BaseActivity extends BaseMvpActivity<BasePresenter> {
    protected String TAG = "BaseActivity";
    protected final BehaviorSubject<ActivityEvent> lifeSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        lifeSubject.onNext(ActivityEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifeSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifeSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifeSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifeSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifeSubject.onNext(ActivityEvent.DESTORY);
    }

    protected <T> LifecycleTransformer<T> bindUntilEvent(ActivityEvent bindEvent) {
        return RxLifecycle.bindUntilEvent(lifeSubject, bindEvent);
    }

    protected void startActivity(Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }
}
