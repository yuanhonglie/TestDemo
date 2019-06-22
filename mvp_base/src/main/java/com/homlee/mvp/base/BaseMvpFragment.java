package com.homlee.mvp.base;

import android.os.Bundle;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author leo.yuan
 */
public class BaseMvpFragment<T extends BasePresenter> extends Fragment implements IView {
    private static final String TAG = "BaseMvpFragment";
    private T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindPresenter();
    }

    private void bindPresenter() {
        Class<T> clazz = (Class<T>) (getClass().getTypeParameters()[0].getClass());
        Log.i(TAG, "bindPresenter: clazz = " + clazz.getSimpleName());
        mPresenter = bindPresenter(clazz);
        onBindPresenter();
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
