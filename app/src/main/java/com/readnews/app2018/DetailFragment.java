package com.readnews.app2018;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.readnews.com.readnews.app2018.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


    private static final String TAG = DetailFragment.class.getSimpleName();
    WebView wvDetail;
    String url = "";
    String name = "";

    public DetailFragment() {
        // Required empty public constructor
    }

    public static Fragment getNewInstance(String name, String url) {
        Fragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("NAME", name);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initUI(View view) {
        wvDetail = (WebView) view.findViewById(R.id.wvDetail);
        Bundle bundle = getArguments();
        WebSettings webSettings = wvDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if(bundle != null) {
            url = bundle.getString("URL", "");
            name = bundle.getString("NAME", "");
        }
        if(!url.equals("")) {
            Log.d(TAG, "url:"+ url);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(name);
            wvDetail.loadUrl(url);
        }
    }

}
