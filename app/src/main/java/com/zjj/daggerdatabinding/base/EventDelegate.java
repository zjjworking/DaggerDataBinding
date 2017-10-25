package com.zjj.daggerdatabinding.base;

import android.view.View;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by zjj on 17/10/20.
 */

public interface EventDelegate {
    void addData(int length);
    void clear();

    void stopLoadMore();
    void pauseLoadMore();
    void resumeLoadMore();

    void setMore(View view, BaseNewBindingAdapter.OnLoadMoreListener listener);
    void setNoMore(View view);
    void setErrorMore(View view);
}
