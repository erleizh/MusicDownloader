package com.erlei.musicdownloader.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;

import com.erlei.musicdownloader.R;
import com.erlei.musicdownloader.base.BaseActivity;
import com.erlei.musicdownloader.base.BaseFragment;
import com.erlei.musicdownloader.base.Contract;
import com.erlei.musicdownloader.module.downlaod.DownloadFragment;
import com.erlei.musicdownloader.module.search.SearchFragment;
import com.orhanobut.logger.Logger;

import java.util.Objects;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SparseArray<BaseFragment> mFragments = new SparseArray<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(View rootView) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_search);
        onNavigationItemSelected(Objects.requireNonNull(navigationView.getCheckedItem()));
    }


    @Nullable
    @Override
    public Contract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (!getCurrentFragment().onBackPressed()) {
                super.onBackPressed();
            }
        }
    }


    public BaseFragment getCurrentFragment() {
        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
    }

    private BaseFragment createFragment(int id) {
        BaseFragment fragment = mFragments.get(id);
        if (fragment != null) return fragment;
        switch (id) {
            case R.id.nav_download:
                fragment = DownloadFragment.newInstance();
                break;
            case R.id.nav_settings:
            case R.id.nav_search:
            default:
                fragment = SearchFragment.newInstance();
        }
        mFragments.put(id, fragment);
        return fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Logger.t(TAG).d("onNavigationItemSelected id = ", item.getItemId());

        BaseFragment fragment = createFragment(item.getItemId());
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragmentByTag = manager.findFragmentByTag(fragment.getClass().getName());
        FragmentTransaction beginTransaction = manager.beginTransaction();
        if (fragmentByTag == null)
            beginTransaction.add(R.id.content_main, fragment, fragment.getClass().getName());
        for (Fragment fragment1 : manager.getFragments()) {
            if (fragment != fragment1) beginTransaction.hide(fragment1);
        }
        beginTransaction.show(fragment).commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
