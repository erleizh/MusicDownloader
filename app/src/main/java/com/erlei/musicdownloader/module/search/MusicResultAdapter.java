package com.erlei.musicdownloader.module.search;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.erlei.musicdownloader.R;
import com.erlei.musicdownloader.base.BaseCheckableQuickAdapter;
import com.erlei.musicdownloader.base.GlideApp;
import com.erlei.musicdownloader.http.responses.MusicBean;

public class MusicResultAdapter extends BaseCheckableQuickAdapter<MusicBean, BaseViewHolder> {

    public MusicResultAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicBean item) {
        helper.setText(R.id.tv_music_author, item.getAuthor());
        helper.setText(R.id.tv_music_name, item.getTitle());
        GlideApp.with(mContext).load(item.getPic()).centerCrop().into((ImageView) helper.getView(R.id.iv_pic));

        CheckBox view = helper.getView(R.id.cb_checked);
        view.setChecked(isChecked(helper.getAdapterPosition()));
        view.setOnCheckedChangeListener((buttonView, isChecked) -> setChecked(helper.getAdapterPosition(), isChecked));
    }
}
