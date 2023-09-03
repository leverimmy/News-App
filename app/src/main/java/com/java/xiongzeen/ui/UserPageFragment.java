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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { //这个函数来自2022年科协暑培的代码
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_page, container, false);
        Button historyButton = view.findViewById(R.id.history_button);
        Button favoriteButton = view.findViewById(R.id.favorite_button);
        historyButton.setOnClickListener(v -> historyButtonClicked());
        favoriteButton.setOnClickListener(v -> favoriteButtonClicked());

        return view;
    }

    public void historyButtonClicked() {

        Log.d("UserPageFragment", "historyButtonClicked");
        MyApplication.userPage = false;
        MyApplication.historyPage = true;
        Bundle mode_config = new Bundle();
        mode_config.putBoolean("mode", false);
        Utils.replaceFragment(this, RecordListFragment.class, mode_config);
    }

    public void favoriteButtonClicked() {

        Log.d("UserPageFragment", "favoriteButtonClicked");
        MyApplication.userPage = false;
        MyApplication.favoritePage = true;
        Bundle mode_config = new Bundle();
        mode_config.putBoolean("mode", true);
        Utils.replaceFragment(this, RecordListFragment.class ,mode_config);
    }
}