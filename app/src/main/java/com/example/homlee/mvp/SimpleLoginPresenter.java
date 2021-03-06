package com.example.homlee.mvp;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.homlee.mvp.model.LoginHelper;
import com.example.homlee.mvp.model.User;
import com.homlee.mvp.base.BasePresenter;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * @author leo.yuan
 */
public class SimpleLoginPresenter
        extends BasePresenter<SimpleLoginContract.Presenter, SimpleLoginContract.View>
        implements SimpleLoginContract.Presenter {

    public SimpleLoginPresenter(@NonNull Application application) {
        super(application);
    }

    @Override
    public void login(final String name, final String password) {
        showLoading();

        LoginHelper.getLoginResultObservable(name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                //.compose(this.<Boolean>bindUntilEvent(PresenterEvent.CLEAR))
                .filter(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            hideLoading();
                            mView.showError("用户名不存在!");
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
                        mView.showSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoading();
                        mView.showError("用户名不存在!!");
                    }
                });
    }

    public void showLoading() {
        mView.showLoading();
    }

    public void hideLoading() {
        mView.hideLoading();
    }
}
