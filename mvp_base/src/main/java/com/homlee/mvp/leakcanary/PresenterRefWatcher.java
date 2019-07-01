package com.homlee.mvp.leakcanary;

import com.homlee.mvp.base.BasePresenter;
import com.homlee.mvp.base.PresenterLifecycle;
import com.homlee.mvp.base.PresenterLifecycleCallback;
import com.homlee.mvp.base.PresenterLifecycleCallbackAdapter;
import com.squareup.leakcanary.RefWatcher;

public class PresenterRefWatcher {
    private RefWatcher refWatcher;
    private PresenterRefWatcher(RefWatcher refWatcher) {
        this.refWatcher = refWatcher;
    }

    public static void install(final RefWatcher refWatcher) {
        PresenterRefWatcher presenterRefWatcher = new PresenterRefWatcher(refWatcher);
        PresenterLifecycle.getInstance().register(presenterRefWatcher.callback);
    }

    private PresenterLifecycleCallback callback = new PresenterLifecycleCallbackAdapter() {
        @Override
        public void onCleared(BasePresenter presenter) {
            refWatcher.watch(presenter);
        }
    };

    public void watchPresenters() {
        stopWatchingPresenters();
        PresenterLifecycle.getInstance().register(callback);
    }

    public void stopWatchingPresenters() {
        PresenterLifecycle.getInstance().unregister(callback);
    }
}
