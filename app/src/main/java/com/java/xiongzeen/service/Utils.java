package com.java.xiongzeen.service;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.R;
import com.java.xiongzeen.data.Category;

import java.util.ArrayList;
import java.util.List;

public final class Utils {

    public static void replaceFragment(Fragment fragment, Class<? extends Fragment> fragmentClass, Bundle data) {
        fragment.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, data)
                .addToBackStack(null)
                .commit();
    }

    public static String categoryListToString(List<Category> input) {
        StringBuilder ans = new StringBuilder();
        for(Category category : input) {
            ans.append(category.name());
            ans.append(",");
        }
        String to_return = ans.length() == 0 ?
                "" : ans.substring(0, ans.length() - 1);
        Log.d("Utils", "categoryListToString = " + to_return);
        return to_return;
    }

    public static List<Category> stringToCategoryList(String input) {
        Log.d("Utils", "stringToCategoryList = " + input);
        try {
            String[] categories = input.split(",");
            List<Category> ans = new ArrayList<>();
            for(String category : categories) {
                ans.add(Category.valueOf(category));
            }
            return ans;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}