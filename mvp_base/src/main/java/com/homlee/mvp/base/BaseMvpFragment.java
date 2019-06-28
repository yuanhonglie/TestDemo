package com.homlee.mvp.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author leo.yuan
 */
public class BaseMvpFragment<T extends BasePresenter> extends Fragment implements IView {
    private static final String TAG = "BaseMvpFragment";
    private T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rBindPresenter(getClass());
    }

    private void bindPresenterInner(Class clazz) {
        mPresenter = bindPresenter((Class<T>) clazz);
        onBindPresenter();
    }

    private void rBindPresenter(Class<?> clazz) {
        Class<?> superClazz = clazz.getSuperclass();
        Type superType = clazz.getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            Type[] argTypes = ((ParameterizedType) superType).getActualTypeArguments();
            for (int i = 0; i < argTypes.length; i++) {
                Type argType = argTypes[i];
                if (argType instanceof Class) {
                    try {
                        Class<T> argClazz = (Class<T>) argType.getClass();
                        bindPresenterInner((Class) argType);
                        return;
                    } catch (ClassCastException e) {
                        Log.e(TAG, "rBindPresenter: error = " + e.getMessage());
                    }
                }
            }
            rBindPresenter(superClazz);
        } else if (!superClazz.equals(Object.class)) {
            rBindPresenter(superClazz);
        }
    }

    protected <P extends BasePresenter> P bindPresenter(Class<P> clazz) {
        return (P) ViewModelProviders.of(this).get(clazz).attachView(this);
    }

    protected void onBindPresenter() {

    }

    private void unbindPresenter() {
        mPresenter.detachView();
        onUnbindPresenter();
    }

    protected void onUnbindPresenter() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindPresenter();
    }
}
