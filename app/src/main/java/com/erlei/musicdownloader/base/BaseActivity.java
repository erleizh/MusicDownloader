package com.erlei.musicdownloader.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;

/**
 * Created by lll on 2018/3/22.
 * Email : lllemail@foxmail.com
 */
public abstract class BaseActivity<P extends Contract.Presenter> extends AppCompatActivity implements Contract.View {

    public P mPresenter;
    protected final String TAG = this.getClass().getName();
    private boolean mActive = false;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActive = true;
        View rootView = getLayoutInflater().inflate(this.getLayoutId(), null, false);
        this.setContentView(rootView);
        mPresenter = initPresenter();
        initView(rootView);
        if (getPresenter() != null) getPresenter().onAttached();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActive = false;
        showLoading(false);
        if (getPresenter() != null) getPresenter().onDetached();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View rootView);

    @Nullable
    public abstract P initPresenter();

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showDialogMessage(String msg) {


    }

    @Override
    public boolean isActive() {
        return mActive;
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getContext());
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }

    }


    @Override
    public void showToastMessage(String msg) {
        ToastUtils.showLong(msg);
    }
}
