package com.erlei.musicdownloader.module.search;

import com.erlei.musicdownloader.http.RetrofitUtil;
import com.erlei.musicdownloader.http.requests.SearchMusicRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@PrepareForTest({RetrofitUtil.class})
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "org.json.*", "sun.security.*", "javax.net.*"})
public class SearchPresenterTest {


    @Mock
    SearchContract.View mView;
    private SearchPresenter mPresenter;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mView.isActive()).thenReturn(true);
    }

    @Test
    public void createPresenter() {
        mPresenter = new SearchPresenter(mView);


        SearchMusicRequest request = new SearchMusicRequest();
        request.setFilter("name");
        request.setSource("qq");
        request.setPage(1);
        request.setInput("周杰伦");
        mPresenter.searchMusic(request);
    }

}