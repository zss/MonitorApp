package com.oumiao.monitor.adapter;

import android.support.v7.widget.RecyclerView;

import com.oumiao.monitor.R;
import com.oumiao.monitor.base.BaseRecycleAdapter;


/**
 * Description:
 * Author:zss
 * Date:17/2/9
 */

public class IndexAdapter extends BaseRecycleAdapter {
    public static final int ITEM_TYPE_HEAD = 1;
    public IndexAdapter(){
        super(R.layout.list_cell_footer);
    }
    @Override
    public void convert(RecyclerView.ViewHolder holder, int position) {

    }

}
