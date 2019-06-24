package com.homlee.mvp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;

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

    private void bindPresenter(ParameterizedType type) {
        mPresenter = bindPresenter((Class<T>) (type).getActualTypeArguments()[0]);
        onBindPresenter();
    }

    private void rBindPresenter(Class<?> clazz) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz.equals(BaseMvpActivity.class)) {
            bindPresenter((ParameterizedType) clazz.getGenericSuperclass());
        } else if (!superClazz.equals(Object.class)) {
            rBindPresenter(superClazz);
        }
    }

    protected <P extends BasePresenter> P bindPresenter(Class<P> clazz) {
        return (P) ViewModelProviders.of(this).get(clazz).bind(this);
    }

    protected void onBindPresenter() {

    }

    private void unbindPresenter() {
        mPresenter.unbind();
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
