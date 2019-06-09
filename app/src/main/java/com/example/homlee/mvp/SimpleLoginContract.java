package com.example.homlee.mvp;

import com.example.homlee.mvp.model.User;

public interface SimpleLoginContract {

    interface View {
        /**
         * 显示登陆进度条
         */
        void showLoading();

        /**
         * 隐藏登陆进度条
         */
        void hideLoading();

        /**
         * 登陆过程发生错误
         * @param message 错误信息
         */
        void onError(String message);

        /**
         * 登陆成功
         * @param user 用户信息
         */
        void onSuccess(User user);
    }

    interface Presenter {

        /**
         * 登陆
         * @param userName 用户名
         * @param password 密码
         */
        void login(String userName, String password);
    }
}
