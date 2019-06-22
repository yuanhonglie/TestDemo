package com.homlee.mvp.base;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author leo.yuan
 */
public class BaseMvpActivity<T extends BasePresenter> extends AppCompatActivity implements IView {
    private static final String TAG = "BaseMvpActivity";
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPresenter();
    }

    private void bindPresenter() {
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;
        Type[] types = pt.getActualTypeArguments();
        if (types != null && types.length > 0) {
            Class<T> clazz = (Class<T>) (types[0]);
            mPresenter = bindPresenter(clazz);
            onBindPresenter();
        }
    }

    protected <P extends BasePresenter> P bindPresenter(Class<P> clazz) {
        return (P) ViewModelProviders.of(this).get(clazz).bind(this);
    }

    protected void onBindPresenter() {

    }

    private void unbindPresenter() {
        if (mPresenter != null) {
            mPresenter.unbind();
            onUnbindPresenter();
        }
    }

    protected void onUnbindPresenter() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindPresenter();
    }
}
