package com.homlee.mvp.base;

public interface PresenterLifecycleCallback {

    void onAttachView(BasePresenter presenter);

    void onDetachView(BasePresenter presenter);

    void onCleared(BasePresenter presenter);
}
