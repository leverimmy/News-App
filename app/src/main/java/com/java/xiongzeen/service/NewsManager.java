package com.java.xiongzeen.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.data.News;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NewsManager {
    private final static NewsManager instance = new NewsManager();
    private static Map<String, News> news = new HashMap<>();
    private static Map<String, Long> historyNews = new HashMap<>();
    private static Map<String, Long> favoriteNews = new HashMap<>();
    private static boolean read = false;


    public static void writeHisPreference() {

        Log.d("preferenceTest","his");
        SharedPreferences preferences_his = MyApplication.getContext().getSharedPreferences("his",0);

        String hisJSONString = new Gson().toJson(historyNews);
        preferences_his.edit().putString("his", hisJSONString).apply();

    }

    public static void writeFavPreference() {

        Log.d("preferenceTest","fav");
        SharedPreferences preferences_fav = MyApplication.getContext().getSharedPreferences("fav",0);
        String favJSONString = new Gson().toJson(favoriteNews);
        preferences_fav.edit().putString("fav", favJSONString).apply();

    }

    private static void readNewsFromPreference() {
        Log.d("preferenceTest","read");
        SharedPreferences preferences_his = MyApplication.getContext().getSharedPreferences("his",0);
        SharedPreferences preferences_fav = MyApplication.getContext().getSharedPreferences("fav",0);

        Type type = new TypeToken<Map<String, Long>>(){}.getType();

        String hisJSONString = preferences_his.getString("his", "");
        String favJSONString = preferences_fav.getString("fav", "");

        historyNews = new Gson().fromJson(hisJSONString, type);
        favoriteNews = new Gson().fromJson(favJSONString, type);

        if (historyNews == null)
            historyNews = new HashMap<>();
        if (favoriteNews == null)
            favoriteNews = new HashMap<>();

        Log.d("preferenceTest","read" + historyNews.size() + " " + favoriteNews.size());
    }

    public static void initialize() {
        List<News> temp = DBManager.query();
        for(News item : temp) {
            String newsID = item.getNewsID();
            news.put(newsID, item);
        }
        readNewsFromPreference();
        read = true;
    }

    public List<News> getRecords(boolean mode) { // 0 for history, 1 for favorite

        Log.d("NewsManager", "Trying to get news records.");

        Map<String, Long> operand = mode ? favoriteNews : historyNews;
        List<Map.Entry<String, Long>> entryList = new ArrayList<>(operand.entrySet());

        entryList.sort(Comparator.comparingLong(Map.Entry::getValue));

        List<News> response = new ArrayList<>();

        for (Map.Entry<String, Long> entry : entryList) {
            response.add(getNews(entry.getKey()));
        }

        return response;
    }

    public void favoriteTriggered(String newsID) {

        if (isFavorites(newsID)) {
            favoriteNews.remove(newsID);
            Log.d("NewsManager", "favorite : dislike");
        } else {
            favoriteNews.put(newsID, System.currentTimeMillis());
            Log.d("NewsManager","favorite: like");
        }

        writeFavPreference();
    }

    public void createNews(News currentNews) {

        Log.d("NewsManager", "Created: [Raw Data = ]" + currentNews);

        String newsID = currentNews.getNewsID();
        news.put(newsID, currentNews);
        historyNews.put(newsID, System.currentTimeMillis());
        writeHisPreference();
        DBManager.modify(currentNews);
    }

    public News getNews(String newsID) {
        return news.get(newsID);
    }

    public void newsList(int offset, int pageSize, TaskRunner.Callback<List<News>> callback) {
        TaskRunner.getInstance().execute(() -> applyForNews(offset, pageSize), callback);
    }

    private List<News> applyForNews(int offset, int pageSize) {
        return FetchFromAPIManager.getInstance().getNews(offset, pageSize);
    }

    public static boolean isRead(String newsID) {
        return historyNews.containsKey(newsID);
    }

    public static boolean isFavorites(String newsID) {
        return favoriteNews.containsKey(newsID);
    }

    public static NewsManager getInstance() {
        if (!read)
            initialize();
        return instance;
    }
}

