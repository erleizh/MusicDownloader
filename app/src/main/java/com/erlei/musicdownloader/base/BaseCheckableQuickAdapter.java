package com.erlei.musicdownloader.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class BaseCheckableQuickAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    private boolean[] mChecked;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public BaseCheckableQuickAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        mChecked = new boolean[data == null ? 0 : data.size()];
    }

    public BaseCheckableQuickAdapter(@Nullable List<T> data) {
        super(data);
        mChecked = new boolean[data == null ? 0 : data.size()];
    }

    public BaseCheckableQuickAdapter(int layoutResId) {
        super(layoutResId);
    }

    /**
     * setting up a new instance to data;
     *
     * @param data
     */
    @Override
    public void setNewData(@Nullable List<T> data) {
        mChecked = new boolean[data == null ? 0 : data.size()];
        super.setNewData(data);
    }


    /**
     * add one new data in to certain location
     *
     * @param position
     * @param data
     */
    @Override
    public void addData(int position, @NonNull T data) {
        mChecked = Arrays.copyOf(mChecked, mChecked.length + 1);
        System.arraycopy(mChecked, position, mChecked, position + 1, mChecked.length - 1);
        mChecked[position] = false;
        super.addData(position, data);
    }

    /**
     * add one new data
     *
     * @param data
     */
    @Override
    public void addData(@NonNull T data) {
        mChecked = Arrays.copyOf(mChecked, mChecked.length + 1);
        super.addData(data);
    }


    /**
     * change data
     *
     * @param index
     * @param data
     */
    @Override
    public void setData(int index, @NonNull T data) {
        mChecked[index] = false;
        super.setData(index, data);
    }

    /**
     * add new data in to certain location
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    @Override
    public void addData(int position, @NonNull Collection<? extends T> newData) {
        boolean[] booleans = new boolean[mChecked.length + newData.size()];
        System.arraycopy(mChecked, 0, booleans, 0, position);
        System.arraycopy(mChecked, position, booleans, position + newData.size(), booleans.length - (position + newData.size()));
        mChecked = booleans;
        super.addData(position, newData);
    }

    /**
     * add new data to the end of mData
     *
     * @param newData the new data collection
     */
    @Override
    public void addData(@NonNull Collection<? extends T> newData) {
        mChecked = Arrays.copyOf(mChecked, mChecked.length + newData.size());
        super.addData(newData);
    }

    @Override
    public void remove(int position) {
        boolean[] booleans = new boolean[mChecked.length - 1];
        System.arraycopy(mChecked, 0, booleans, 0, position);
        System.arraycopy(mChecked, position + 1, booleans, position, booleans.length - position);
        mChecked = booleans;
        super.remove(position);
    }

    /**
     * use data to replace all item in mData. this method is different {@link #setNewData(List)},
     * it doesn't change the mData reference
     *
     * @param data data collection
     */
    @Override
    public void replaceData(@NonNull Collection<? extends T> data) {
        mChecked = new boolean[data.size()];
        super.replaceData(data);
    }

    public void setChecked(int index, boolean checked) {
        setChecked(index, checked, false);
    }

    /**
     * @param index   index
     * @param checked 是否选中
     * @param notify  是否需要更新布局
     */
    public void setChecked(int index, boolean checked, boolean notify) {
        mChecked[index] = checked;
        if (notify) notifyItemChanged(index);
    }

    public void checkAll(boolean checked) {
        if (mChecked == null) return;
        Arrays.fill(mChecked, checked);
        notifyDataSetChanged();
    }

    /**
     * 反选
     */
    public void inverse() {
        if (mChecked == null) return;
        for (int i = 0; i < mChecked.length; i++) {
            mChecked[i] = !mChecked[i];
        }
        notifyDataSetChanged();
    }

    public boolean isChecked(int index) {
        return mChecked[index];
    }

    public List<T> getChecked() {
        ArrayList<T> list = new ArrayList<>();
        if (mChecked == null) return list;
        for (int i = 0; i < mChecked.length; i++) {
            if (mChecked[i]) {
                list.add(getItem(i));
            }
        }
        return list;
    }
}
