package com.homlee.mvp.base;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import io.reactivex.subjects.BehaviorSubject;

/**
 * @author leo.yuan
 */
public class BasePresenter<P extends IPresenter, V extends IView> extends AndroidViewModel implements IPresenter<V> {

    protected V mView;
    private WeakReference<V> mViewRef;
    private Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final BehaviorSubject<PresenterEvent> mLifecycleSubject = BehaviorSubject.create();

    public BasePresenter(@NonNull Application application) {
        super(application);
        onInit();
    }

    protected void onInit() {

    }

    public P attachView(V view) {
        Class<?> clazz = findViewInterface(getClass());
        if (clazz == null) {
            throw new RuntimeException("can not find subclass of IView, have you implemented interface com.homlee.mvp.base.IView?");
        } else {
            mViewRef = new WeakReference<>(view);
            // 动态代理V层, 在P层不用每次都判断V层是否为空
            mView = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), new Class<?>[]{clazz},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            // 如果V层没被销毁, 执行V层的方法.
                            if (isViewBound()) {
                                return method.invoke(mViewRef.get(), args);
                            }
                            return null;
                        }
                    });
        }

        onAttachViewInner();
        return (P) this;
    }

    private void onAttachViewInner() {
        sendLifecycleEvent(PresenterEvent.ATTACH);
        PresenterLifecycle.getInstance().onAttachView(this);
        onAttachView();
    }

    /**
     * 绑定View时回调
     */
    protected void onAttachView() {

    }

    /**
     * 从Presenter的泛型参数中查找View接口，用来实现View层的动态代理
     * @param clazz
     * @return View接口类
     */
    private Class<?> findViewInterface(Class<?> clazz) {
        Class<?> superClazz = clazz.getSuperclass();
        Type superType = clazz.getGenericSuperclass();
        if (superType instanceof ParameterizedType) {
            Type[] argTypes = ((ParameterizedType) superType).getActualTypeArguments();
            for (int i = 0; i < argTypes.length; i++) {
                Type argType = argTypes[i];
                if (argType instanceof Class) {
                    Class argClazz = (Class) argType;
                    try {
                        argClazz.asSubclass(IView.class);
                        return argClazz;
                    } catch (ClassCastException e) {
                        Log.e("BasePresenter", "findViewInterface: error = " + e.getMessage());
                    }
                }
            }
            findViewInterface(superClazz);
        } else if (!superClazz.equals(Object.class)) {
            findViewInterface(superClazz);
        }

        return null;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }

        onDetachViewInner();
    }

    private void onDetachViewInner() {
        sendLifecycleEvent(PresenterEvent.DETACH);
        PresenterLifecycle.getInstance().onDetachView(this);
        onDetachView();
    }

    /**
     * 解绑View时回调
     */
    protected void onDetachView() {

    }

    public boolean isViewBound() {
        return mViewRef != null && mViewRef.get() != null;
    }

    private void sendLifecycleEvent(PresenterEvent event) {
        mLifecycleSubject.onNext(event);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onClearedInner();
    }

    private void onClearedInner() {
        sendLifecycleEvent(PresenterEvent.CLEAR);
        //解绑View
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }

        mUiHandler.removeCallbacksAndMessages(null);
        PresenterLifecycle.getInstance().onCleared(this);
    }

    protected final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull PresenterEvent event) {
        return RxLifecycle.bindUntilEvent(mLifecycleSubject, event);
    }

    /**
     * 在Ui线程执行某些操作
     *
     * @param action 需要在UI线程执行的操作
     */
    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            mUiHandler.post(action);
        } else {
            action.run();
        }
    }
}
