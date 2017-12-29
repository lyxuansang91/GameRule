package com.gamecard.rule2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
        gameList.add(new GameItem("Tài xỉu", "https://sanhrong.com/game-tai-xiu/luat-choi"));
        gameList.add(new GameItem("Xóc đĩa", "https://xocdia88.net/huong-dan-luat-choi-xoc-dia-online/"));
        gameList.add(new GameItem("Ba cây", "https://bigkool.net/ba-cay/huong-dan-choi-ba-cay/"));
        gameList.add(new GameItem("Tiến lên miền Nam", "https://bigkool.net/tien-len-mien-nam/huong-dan-choi-tien-len-mien-nam/"));
        gameList.add(new GameItem("Phỏm", "https://bigkool.net/phom-ta-la/huong-dan-choi-phom-ta-la/"));
        gameList.add(new GameItem("Mậu binh", "https://bigkool.net/mau-binh/huong-dan-choi-mau-binh/"));
        gameList.add(new GameItem("Xì tố", "https://bigkool.net/xi-to/huong-dan-choi-xi-to/"));
        gameList.add(new GameItem("Poker", "https://danhbai123.com/huong-dan-cach-choi-bai-poker-luat-choi-poker-co-ban/"));
        gameList.add(new GameItem("Liêng", "https://sanhrong.com/game-lieng/luat-choi"));
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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.app_name));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
