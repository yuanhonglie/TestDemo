package com.example.homlee.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ActionMode;

import com.example.homlee.rxjava.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

public class BaseActivity extends Activity {
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

    protected  <T> ObservableTransformer<T, T> bindUntilEvent(final ActivityEvent bindEvent) {
        //被监视的Observable
        final Observable<ActivityEvent> subject = lifeSubject.takeUntil(new Predicate<ActivityEvent>() {
            @Override
            public boolean test(ActivityEvent event) {
                return event.equals(bindEvent);
            }
        });

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.takeUntil(subject);
            }
        };
    }

    protected void startActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }
}
