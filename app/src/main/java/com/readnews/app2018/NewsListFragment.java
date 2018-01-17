package com.readnews.app2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.readnews.app2018.adapter.NewsListAdapter;
import com.readnews.com.readnews.app2018.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment {


    private RecyclerView rvListItem;

    public NewsListFragment() {
        // Required empty public constructor
    }

    public static Fragment getNewInstance() {
        Fragment fragment = new NewsListFragment();
        return fragment;
    }

    private void initData() {

    }


    private void initUI(View view) {
        rvListItem = (RecyclerView) view.findViewById(R.id.rvListItem);
        rvListItem.setHasFixedSize(true);
        ArrayList<Item> newList = new ArrayList<Item>();
        newList.add(new Item("VNExpress", "https://vnexpress.net/"));
        newList.add(new Item("Dân trí", "http://dantri.com.vn/"));
        newList.add(new Item("Vietnamnet", "http://vietnamnet.vn/"));
        newList.add(new Item("Tinh tế", "https://tinhte.vn/"));
        newList.add(new Item("Kênh 14", "http://kenh14.vn/"));
        newList.add(new Item("Báo mới", "https://baomoi.com/"));
        newList.add(new Item("Zing news", "https://news.zing.vn/"));
        newList.add(new Item("Báo 24h", "http://www.24h.com.vn/"));
        newList.add(new Item("Soha News", "http://soha.vn/"));
        newList.add(new Item("Tin tức online", "http://tintuconline.com.vn/"));
        NewsListAdapter adapter = new NewsListAdapter(getActivity(), newList);
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
