package com.homlee.mvp.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * @author leo.yuan
 */
public class BasePresenter<P extends IPresenter, V extends IView> extends AndroidViewModel {

    protected V mView;

    public BasePresenter(@NonNull Application application) {
        super(application);
    }

    public P bind(V view) {
        mView = view;
        return (P) this;
    }

    public void unbind() {
        mView = null;
    }

    public boolean isViewBound() {
        return mView != null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
