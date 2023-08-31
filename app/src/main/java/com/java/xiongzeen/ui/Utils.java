package com.java.xiongzeen.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.R;
import com.java.xiongzeen.data.Category;
import com.java.xiongzeen.service.NewsManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Utils {

    public static void replaceFragment(Fragment fragment, Class<? extends Fragment> fragmentClass, Bundle data) {
        fragment.getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragmentClass, data)
                .addToBackStack(null)
                .commit();
    }

    public static String stringSetToString(Set<String> input) {
        StringBuilder ans = new StringBuilder();
        for(String value : input) {
            ans.append(value);
            ans.append(",");
        }
        String to_return = new String(ans);
        Log.d("Utils stringListToString", to_return);
        return to_return;
    }

    public static Set<String> stringToStringSet(String input) {

        try {
            Log.d("Utils stringToStringList", input);
            String[] temp = input.split(",");
            Set<String> ans = new HashSet<>();
            for(String sub : temp) {
                ans.add(sub);
            }
            return ans;
        } catch (Exception e) {
            Log.d("Utils stringToStringList", "null");
            return new HashSet<>();
        }
    }

    public static String categoryListToString(List<Category> input) {
        StringBuilder ans = new StringBuilder();
        for(Category value : input){
            ans.append(value.toString());
            ans.append(",");
        }
        String to_return = new String(ans);
        Log.d("Utils categoryListToString", to_return);
        return to_return;
    }

    public static List<Category> stringToCategoryList(String input) {
        Log.d("Utils stringToCategoryList", input);
        try {
            String[] temp = input.split(",");
            List<Category> ans = new ArrayList<>();
            for(String sub : temp){
                ans.add(Category.valueOf(sub));
            }
            return ans;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}