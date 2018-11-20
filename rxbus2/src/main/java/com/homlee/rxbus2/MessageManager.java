package com.homlee.rxbus2;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by homlee on 2018/11/20.
 */

public class MessageManager {
    private static final String TAG = "MessageManager";
    private static volatile MessageManager mInstance;
    private Disposable mDisposable;
    private MessageManager() {
        Observable<MessageEvent> observable = RxBus.get().toObservable(MessageEvent.class);
        mDisposable = observable.subscribe(new Consumer<MessageEvent>() {
            @Override
            public void accept(MessageEvent messageEvent) throws Exception {
                Log.i(TAG, "accept: " + messageEvent.type + ", " + messageEvent.message);
            }
        });
    }

    public static MessageManager getInstance() {
        if (mInstance == null) {
            synchronized (MessageManager.class) {
                if (mInstance == null) {
                    mInstance = new MessageManager();
                }
            }
        }
        return mInstance;
    }


    public void sendMessage(int type, String message) {
        RxBus.get().post(new MessageEvent(type, message));
    }

    public void destroy() {
        mInstance = null;
    }
}
