package com.readnews.app2018.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readnews.app2018.ClickEvent;
import com.readnews.app2018.Item;
import com.readnews.com.readnews.app2018.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sanglx on 12/29/17.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private static final String TAG = NewsListAdapter.class.getSimpleName();
    private Context mContext;
    ArrayList<Item> mList = new ArrayList<Item>();

    public NewsListAdapter(Context mContext, ArrayList<Item> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void add(Item game) {
        this.mList.add(game);
        this.notifyItemInserted(mList.size() - 1);
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ViewHolder holder, int position) {
        String name = mList.get(position).getName();
        holder.tvName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        public ViewHolder(View v) {
            super(v);
            tvName = (TextView) v.findViewById(R.id.tvName);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Log.d(TAG, "on click item");
                    Item item = mList.get(getAdapterPosition());
                    EventBus.getDefault().post(new ClickEvent(item));
                }
            });
        }
    }
}
