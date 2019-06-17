package com.example.homlee.mvp;

import com.example.homlee.mvp.model.User;
import com.example.homlee.mvp.model.LoginHelper;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * @author leo.yuan
 */
public class SimpleLoginPresenter implements SimpleLoginContract.Presenter {
    SimpleLoginContract.View mView;
    public SimpleLoginPresenter(SimpleLoginContract.View view) {
        mView = view;
    }

    @Override
    public void login(final String name, final String password) {
        showLoading();

        LoginHelper.getLoginResultObservable(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            hideLoading();
                            if (mView != null) {
                                mView.showError("用户名不存在!");
                            }
                        }
                        return aBoolean;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<User>>() {
                             @Override
                             public ObservableSource<User> apply(Boolean aBoolean) throws Exception {
                                 return LoginHelper.getUserInfoObservable(name);
                             }
                         }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        hideLoading();
                        if (mView != null) {
                            mView.showSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoading();
                        if (mView != null) {
                            mView.showError("用户名不存在!!");
                        }
                    }
                });
    }

    public void showLoading() {
        if (mView != null) {
            mView.showLoading();
        }
    }

    public void hideLoading() {
        if (mView != null) {
            mView.hideLoading();
        }
    }
}
