package com.frxs.order.adapter;

import android.content.Context;

import com.ewu.core.widget.PinnedSectionListView;
import com.frxs.order.model.SectionListItem;
import com.pacific.adapter.Adapter;

import java.util.List;

/**
 * Created by ewu on 2016/5/27.
 */
public abstract class PinnedSectionProductListAdapter extends Adapter<SectionListItem> implements PinnedSectionListView.PinnedSectionListAdapter {
    public PinnedSectionProductListAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public PinnedSectionProductListAdapter(Context context, int layoutResId, List<SectionListItem> data) {
        super(context, data, layoutResId);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return ((SectionListItem) getItem(position)).type;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == SectionListItem.SECTION;
    }

}
