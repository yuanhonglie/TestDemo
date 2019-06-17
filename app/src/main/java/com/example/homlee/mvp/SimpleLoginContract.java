package com.example.homlee.mvp;

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
        void showError(String message);

        /**
         * 登陆成功
         */
        void showSuccess();
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
