package com.erlei.musicdownloader.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public abstract class BaseFragment<P extends Contract.Presenter> extends Fragment implements Contract.View {

    public P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        if (getPresenter() != null) getPresenter().onAttached();
    }

    protected abstract void initView(View view);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) getPresenter().onDetached();
    }


    @Override
    public void showToastMessage(String msg) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) ((BaseActivity) activity).showToastMessage(msg);
    }

    @Override
    public void showDialogMessage(String msg) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) ((BaseActivity) activity).showDialogMessage(msg);
    }

    @Override
    public void showLoading(boolean show) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) ((BaseActivity) activity).showLoading(show);
    }

    /**
     * @return 消费了 true ，不消费 false
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    protected abstract P initPresenter();

}
