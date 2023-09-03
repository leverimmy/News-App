package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.data.Category;
import com.java.xiongzeen.R;

import java.util.ArrayList;
import java.util.List;


public class TabListFragment extends Fragment {

    public List<String> tabs = new ArrayList<>();
    private View view;
    private TabLayout tabLayout;
    private CheckBox selectMenu;
    private onTabBarListener mListener;


    public interface onTabBarListener {
        void menuBarClicked();
        void tabSelected(String tag);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onTabBarListener) {
            mListener = (onTabBarListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_list, container, false);
        tabLayout = view.findViewById(R.id.subject_tabs);
        selectMenu = view.findViewById(R.id.edit_menu);

        selectMenu.setOnClickListener(view -> {
            if(mListener != null) {
                mListener.menuBarClicked();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mListener.tabSelected(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        updateList();
        return view;
    }

    public void updateList() {
        Log.d("TabListFragment", "update called");
        tabs.clear();
        tabs.add("综合");
        if(MyApplication.myUser.selected != null) {
            for(Category a : MyApplication.myUser.selected) {
                tabs.add(a.name());
            }
        }
        tabLayout.removeAllTabs();
        for(int i = 0; i < tabs.size(); i++){
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(tabs.get(i));
            tabLayout.addTab(tab);
        }
    }
}