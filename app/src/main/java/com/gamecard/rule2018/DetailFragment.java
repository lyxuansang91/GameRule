package com.gamecard.rule2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    WebView wvDetail;
    int zoneId = -1;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static Fragment getNewInstance(int zoneId) {
        Fragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ZONE_ID", zoneId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        initUI(rootView);
        return rootView;
    }

    private void initUI(View view) {
        wvDetail = (WebView) view.findViewById(R.id.wvDetail);
        Bundle bundle = getArguments();
        WebSettings webSettings = wvDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(bundle != null) {
            zoneId = bundle.getInt("ZONE_ID");
        }
        if(zoneId != -1) {
            wvDetail.loadUrl();
        }
    }

}
