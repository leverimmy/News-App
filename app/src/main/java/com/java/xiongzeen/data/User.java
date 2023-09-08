package com.java.xiongzeen.data;


import android.content.SharedPreferences;
import android.util.Log;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.service.Utils;

import java.util.ArrayList;
import java.util.List;


public class User {

    private final String name = "DemoUser";

    public List<Category> selected = new ArrayList<>();
    public List<Category> unselected = new ArrayList<>();


    public void writeSelectPreference() {
        Log.d("preferenceTest","write");
        SharedPreferences preferences_selected =
                MyApplication.getContext().getSharedPreferences("selected",0);
        SharedPreferences preferences_unselected =
                MyApplication.getContext().getSharedPreferences("unselected",0);

        preferences_selected.edit().putString("selected", Utils.categoryListToString(selected)).apply();
        preferences_unselected.edit().putString("unselected", Utils.categoryListToString(unselected)).apply();
    }

    private void readSelectFromPreference() {
        Log.d("preferenceTest","read");
        SharedPreferences preferences_selected =
                MyApplication.getContext().getSharedPreferences("selected",0);
        SharedPreferences preferences_unselected =
                MyApplication.getContext().getSharedPreferences("unselected",0);

        selected = Utils.stringToCategoryList(preferences_selected.getString("selected",""));
        unselected = Utils.stringToCategoryList(preferences_unselected.getString("unselected",""));

        Log.d("preferenceTest","read" + selected.size() + " " + unselected.size());

    }

    public User() {
        readSelectFromPreference();
        if (selected.size() == 0 && unselected.size() == 0) {
            selected.add(Category.教育);
            selected.add(Category.娱乐);
            selected.add(Category.科技);
            selected.add(Category.体育);
            selected.add(Category.健康);
            selected.add(Category.军事);
            selected.add(Category.文化);
            selected.add(Category.汽车);
            selected.add(Category.社会);
            selected.add(Category.财经);
        }
    }

    protected void finalize() {
        writeSelectPreference();
    }
}
