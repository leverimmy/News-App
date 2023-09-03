package com.java.xiongzeen.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;


public class UserPageFragment extends Fragment {

    private View mView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //这个函数来自2022年科协暑培的代码
        // Inflate the layout for this fragment
        if (mView != null) {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null)
                group.removeView(mView);
        }

        mView = inflater.inflate(R.layout.fragment_user_page, container, false);

        Button historyButton = mView.findViewById(R.id.history_button);
        Button favoriteButton = mView.findViewById(R.id.favorite_button);
        historyButton.setOnClickListener(v -> history_button_click());
        favoriteButton.setOnClickListener(v -> favorite_button_click());

        return mView;
    }

    private void history_button_click() {

        Log.d("history button", "click");
        MyApplication.userPage = false;
        MyApplication.historyPage = true;
        Bundle mode_config = new Bundle();
        mode_config.putBoolean("mode", false);
        Utils.replaceFragment(this, RecordListFragment.class, mode_config);
    }

    private void favorite_button_click() {

        Log.d("favorite button", "click");
        MyApplication.userPage = false;
        MyApplication.favoritePage = true;
        Bundle mode_config = new Bundle();
        mode_config.putBoolean("mode", true);
        Utils.replaceFragment(this, RecordListFragment.class ,mode_config);
    }
}