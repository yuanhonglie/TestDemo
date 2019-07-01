package com.homlee.mvp.base;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * @author leo.yuan
 */
public class PresenterLifecycle implements PresenterLifecycleCallback {
    private static final String TAG = "PresenterLifecycle";
    private static PresenterLifecycle mInstance = new PresenterLifecycle();
    private List<PresenterLifecycleCallback> callbacks = new LinkedList<>();
    private PresenterLifecycle(){}

    public static PresenterLifecycle getInstance() {
        return mInstance;
    }

    public void register(PresenterLifecycleCallback callback) {
        synchronized (callbacks) {
            callbacks.add(callback);
        }
    }
    
    public void unregister(PresenterLifecycleCallback callback) {
        synchronized (callbacks) {
            callbacks.remove(callback);
        }
    }

    @Override
    public void onAttachView(BasePresenter presenter) {
        Log.i(TAG, "onAttachView: ");
        for (PresenterLifecycleCallback cb : callbacks) {
            cb.onAttachView(presenter);
        }
    }

    @Override
    public void onDetachView(BasePresenter presenter) {
        Log.i(TAG, "onDetachView: ");
        for (PresenterLifecycleCallback cb : callbacks) {
            cb.onDetachView(presenter);
        }
    }

    @Override
    public void onCleared(BasePresenter presenter) {
        Log.i(TAG, "onCleared: ");
        for (PresenterLifecycleCallback cb : callbacks) {
            cb.onCleared(presenter);
        }
    }
}
