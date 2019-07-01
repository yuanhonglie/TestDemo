package com.example.homlee.mvp.model;

import com.example.homlee.mvp.model.User;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author leo.yuan
 */
public class LoginHelper {

    private LoginHelper() {}

    public static Observable<User> getUserInfoObservable(String userName) {
        return Observable.just(new User(userName, 25, "SAP", "Android APP"))
                .delay(2, TimeUnit.SECONDS);
    }

    public static Observable<Boolean> getLoginResultObservable(String username, String password) {

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                Random random = new Random(System.currentTimeMillis());
                emitter.onNext(random.nextBoolean());
                emitter.onComplete();
            }
        }).delay(300, TimeUnit.SECONDS);
    }
}
