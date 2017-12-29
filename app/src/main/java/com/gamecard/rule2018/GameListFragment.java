package com.gamecard.rule2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gamecard.rule2018.adapter.GameListAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameListFragment extends Fragment {


    private RecyclerView rvListItem;

    public GameListFragment() {
        // Required empty public constructor
    }

    public static Fragment getNewInstance() {
        Fragment fragment = new GameListFragment();
        return fragment;
    }

    private void initData() {

    }


    private void initUI(View view) {
        rvListItem = (RecyclerView) view.findViewById(R.id.rvListItem);
        rvListItem.setHasFixedSize(true);
        ArrayList<GameItem> gameList = new ArrayList<GameItem>();
        gameList.add(new GameItem("Tài xỉu", 17));
        gameList.add(new GameItem("Xóc đĩa", 15));
        gameList.add(new GameItem("Ba cây", 1));
        gameList.add(new GameItem("Tiến lên miền Nam", 5));
        gameList.add(new GameItem("Phỏm", 4));
        gameList.add(new GameItem("Mậu binh", 12));
        gameList.add(new GameItem("Xì tố", 2));
        gameList.add(new GameItem("Poker", 3));
        GameListAdapter adapter = new GameListAdapter(getActivity(), gameList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvListItem.setAdapter(adapter);
        rvListItem.setLayoutManager(layoutManager);
        rvListItem.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);
        initData();
        initUI(view);
        return view;
    }

}
