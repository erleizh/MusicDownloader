package com.erlei.musicdownloader.module.downlaod;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.erlei.musicdownloader.R;
import com.erlei.musicdownloader.base.BaseFragment;

public class DownloadFragment extends BaseFragment<DownloadContract.Presenter> implements DownloadContract.View {

    public static DownloadFragment newInstance() {
        Bundle args = new Bundle();
        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    protected void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_download);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Nullable
    @Override
    public DownloadContract.Presenter initPresenter() {
        return new DownloadPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


}
