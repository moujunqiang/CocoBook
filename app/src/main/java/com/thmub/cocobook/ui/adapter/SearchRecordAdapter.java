package com.thmub.cocobook.ui.adapter;

import com.thmub.cocobook.base.adapter.BaseListAdapter;
import com.thmub.cocobook.base.adapter.IViewHolder;
import com.thmub.cocobook.ui.adapter.view.HotKeyWordHolder;
import com.thmub.cocobook.ui.adapter.view.SearchRecordHolder;

/**
 * Created by zhouas666 on 17-6-2.
 */

public class SearchRecordAdapter extends BaseListAdapter<String> {
    @Override
    protected IViewHolder<String> createViewHolder(int viewType) {
        return new SearchRecordHolder();
    }
}
