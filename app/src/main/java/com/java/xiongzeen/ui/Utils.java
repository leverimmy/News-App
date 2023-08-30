package com.java.xiongzeen.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.R;
import com.java.xiongzeen.service.NewsManager;

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

    public static boolean is_an_api_id_read(String id_from_API) {
        return NewsManager.convert_id(id_from_API) >= 0;
    }

    public static String listToString(List<Long> input) {
        StringBuilder ans = new StringBuilder();
        for(Long value : input){
            ans.append(value.toString());
            ans.append(",");
        }
        String to_return = new String(ans);
        Log.d("utilList_to_string", to_return);
        return to_return;
    }

    public static List<Long> stringToList(String input) {
        Log.d("utilStringToList", input);
        try {
            String[] temp = input.split(",");
            List<Long> ans = new ArrayList<>();
            for(String sub : temp){
                ans.add(Long.valueOf(sub));
            }
            return ans;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}