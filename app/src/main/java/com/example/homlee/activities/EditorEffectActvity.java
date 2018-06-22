package com.example.homlee.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.homlee.R;
import com.example.homlee.widgets.FocusIndicator;

public class EditorEffectActvity extends Activity implements View.OnClickListener{

    private EditText mAccountEt;
    private EditText mPasswordEt;
    private FocusIndicator mAccountIndicator;
    private FocusIndicator mPasswordIndicator;

    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ui_editor);

        mAccountEt = findViewById(R.id.et_account);
        mPasswordEt = findViewById(R.id.et_password);
        mAccountEt.setOnFocusChangeListener(mListener);
        mPasswordEt.setOnFocusChangeListener(mListener);

        mAccountIndicator = findViewById(R.id.fi_indicator1);
        mPasswordIndicator = findViewById(R.id.fi_indicator2);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        findViewById(R.id.parent).setOnClickListener(this);
        findViewById(R.id.wrapper_editor1).setOnClickListener(this);
        findViewById(R.id.wrapper_editor2).setOnClickListener(this);
        mAccountEt.setOnClickListener(this);
        mPasswordEt.setOnClickListener(this);
    }

    private View.OnFocusChangeListener mListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (view == mAccountEt) {
                mAccountIndicator.setFocus(b);
            } else if (view == mPasswordEt) {
                mPasswordIndicator.setFocus(b);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        ativateEditor(mAccountEt);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.parent:
                deativateEditor(mAccountEt);
                deativateEditor(mPasswordEt);
                break;
            case R.id.et_account:
            case R.id.wrapper_editor1:
                deativateEditor(mPasswordEt);
                ativateEditor(mAccountEt);
                break;
            case R.id.et_password:
            case R.id.wrapper_editor2:
                deativateEditor(mAccountEt);
                ativateEditor(mPasswordEt);
                break;
        }
    }

    private void ativateEditor(EditText editText) {
        editText.setFocusable(true);//设置输入框可聚集
        editText.setFocusableInTouchMode(true);//设置触摸聚焦
        editText.requestFocus();//请求焦点
        editText.findFocus();//获取焦点
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);// 显示输入法
    }

    private boolean deativateEditor(EditText editText) {
        if (editText.isFocusable()) {
            editText.setFocusable(false);//设置输入框不可聚焦，即失去焦点和光标
            if (mInputMethodManager.isActive()) {
                mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);// 隐藏输入法
            }
            return true;
        }
        return false;
    }
}
