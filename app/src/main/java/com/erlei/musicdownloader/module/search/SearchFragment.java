package com.erlei.musicdownloader.module.search;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.erlei.musicdownloader.R;
import com.erlei.musicdownloader.base.BaseFragment;
import com.erlei.musicdownloader.http.requests.SearchMusicRequest;
import com.erlei.musicdownloader.http.responses.MusicBean;

import java.util.List;

public class SearchFragment extends BaseFragment<SearchContract.Presenter> implements SearchContract.View {

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private MusicResultAdapter mResultAdapter;
    private SearchMusicRequest mSearchMusicRequest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);

        RecyclerView recyclerView = view.findViewById(R.id.rv_search_result);
        mResultAdapter = new MusicResultAdapter(R.layout.item_search_music_result);
        mResultAdapter.setPreLoadNumber(1);
        mResultAdapter.setOnLoadMoreListener(() -> getPresenter().loadMore(mSearchMusicRequest), recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mResultAdapter);

        mSearchMusicRequest = new SearchMusicRequest();
        mSearchMusicRequest.setFilter("name");
        mSearchMusicRequest.setSource("qq");
    }


    @Nullable
    @Override
    public SearchContract.Presenter initPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_music, menu);
        initSearchAction(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.selectAll:
                mResultAdapter.checkAll(true);
                return true;
            case R.id.cancelAll:
                mResultAdapter.checkAll(false);
                return true;
            case R.id.inverseSelection:
                mResultAdapter.inverse();
                return true;
            case R.id.download:
                checkPermission();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkPermission() {
        Activity context = getActivity();
        if (context == null) return;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(context, permissions[0]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permissions[1]) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, permissions[2]) == PackageManager.PERMISSION_GRANTED
        ) {
            getPresenter().download(mResultAdapter.getChecked());
        } else {
            ActivityCompat.requestPermissions(context, permissions, 1);
        }
    }

    private void initSearchAction(Menu menu) {
        final MenuItem item = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchMusicRequest.setInput(query);
                getPresenter().searchMusic(mSearchMusicRequest);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    /**
     * 显示搜索音乐的结果
     *
     * @param response result
     */
    @Override
    public void showSearchResult(@NonNull List<MusicBean> response) {
        mResultAdapter.setNewData(response);
    }

    @Override
    public void showLoading(boolean show) {
        super.showLoading(show);


    }

    /**
     * 加载更多
     *
     * @param musics result
     */
    @Override
    public void loadMoreResult(@NonNull List<MusicBean> musics) {
        mResultAdapter.addData(musics);
        mResultAdapter.loadMoreComplete();
    }


    /**
     * 加载更多结束
     */
    @Override
    public void loadMoreFinish() {
        mResultAdapter.loadMoreEnd();
    }

    /**
     * 搜索结果为空
     */
    @Override
    public void showEmptyResult() {
        mResultAdapter.setNewData(null);
    }
}
