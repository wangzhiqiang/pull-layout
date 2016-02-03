package com.dianjineng.android.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dianjineng.android.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wzq <wangzhiqiang951753@gmail.com>
 *         Date : 16-2-3-02-03 15:05
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder> {

    private Context mContext;

    private List<String> mDataList = new ArrayList<>();



    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = View.inflate(mContext, R.layout.list_item, null);
        LinearLayout.LayoutParams lp_l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        item.setLayoutParams(lp_l);
        return new ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.name.setText(mDataList.get(position));
        holder.num.setText(""+position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void notifyDataSetChanged(List<String> data,boolean isRefresh){
        if(data == null ) return;
        if(isRefresh){
            mDataList.clear();
        }
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.num)
        TextView num;
        @Bind(R.id.name)
        TextView name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
