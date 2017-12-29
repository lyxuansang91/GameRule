package com.gamecard.rule2018.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gamecard.rule2018.GameClickEvent;
import com.gamecard.rule2018.GameItem;
import com.gamecard.rule2018.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by sanglx on 12/29/17.
 */

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    private static final String TAG = GameListAdapter.class.getSimpleName();
    private Context mContext;
    ArrayList<GameItem> mGameList = new ArrayList<GameItem>();

    public GameListAdapter(Context mContext, ArrayList<GameItem> mGameList) {
        this.mContext = mContext;
        this.mGameList = mGameList;
    }

    public void add(GameItem game) {
        this.mGameList.add(game);
        this.notifyItemInserted(mGameList.size() - 1);
    }

    @Override
    public GameListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameListAdapter.ViewHolder holder, int position) {
        String gameName = mGameList.get(position).getGameName();
        holder.tvGameName.setText(gameName);
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGameName;
        public ViewHolder(View v) {
            super(v);
            tvGameName = (TextView) v.findViewById(R.id.tvGameName);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "on click item");
                    GameItem gameItem = mGameList.get(getAdapterPosition());
                    EventBus.getDefault().post(new GameClickEvent(gameItem));
                }
            });
        }
    }
}
