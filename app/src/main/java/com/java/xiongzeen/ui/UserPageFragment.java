package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.R;
import com.java.xiongzeen.service.NewsManager;


public class UserPageFragment extends Fragment {

    private Context context;

    public UserPageFragment() {
        // Required empty public constructor
    }

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
        historyButton.setOnClickListener(v -> {history_button_click();});
        favoriteButton.setOnClickListener(v -> {favorite_button_click();});
        context = view.getContext();

        return view;
    }

    public void history_button_click() {
        Toast.makeText(context, "click history", Toast.LENGTH_SHORT);
        Log.d("history button", "click");
        Bundle mode_config = new Bundle();
        mode_config.putInt("mode", 0);
        Utils.replaceFragment(this, RecordListFragment.class, mode_config);
    }

    public void favorite_button_click() {
        Toast.makeText(context, "click favorites", Toast.LENGTH_SHORT);
        Log.d("favorite button", "click");
        Bundle mode_config = new Bundle();
        mode_config.putInt("mode", 1);
        Utils.replaceFragment(this, RecordListFragment.class ,mode_config);
    }
}