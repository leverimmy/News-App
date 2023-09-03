package com.java.xiongzeen.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.data.News;
import com.java.xiongzeen.ui.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class NewsManager {
    private final static NewsManager instance = new NewsManager();
    private static Map<String, News> news = new HashMap<>();
    private static Set<String> historyNews = new HashSet<>();
    private static Set<String> favoriteNews = new HashSet<>();
    private static boolean read = false;

    public static void writeFavPreference() {
        Log.d("preferenceTest","fav");
        SharedPreferences preferences_fav = MyApplication.getContext().getSharedPreferences("fav",0);

        preferences_fav.edit().putString("fav", Utils.stringSetToString(favoriteNews)).apply();

    }
    public static void writeHisPreference() {
        Log.d("preferenceTest","his");
        SharedPreferences preferences_his = MyApplication.getContext().getSharedPreferences("his",0);

        preferences_his.edit().putString("his", Utils.stringSetToString(historyNews)).apply();

    }

    private static void readNewsFromPreference() {
        Log.d("preferenceTest","read");
        SharedPreferences preferences_his = MyApplication.getContext().getSharedPreferences("his",0);
        SharedPreferences preferences_fav = MyApplication.getContext().getSharedPreferences("fav",0);

        historyNews = Utils.stringToStringSet(preferences_his.getString("his",null));
        favoriteNews = Utils.stringToStringSet(preferences_fav.getString("fav",null));

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
        List<News> response = new ArrayList<>();
        if(!mode) {
            for(String l : historyNews) {
                News temp = news.get(l);
                if(temp != null) {
                    response.add(temp);
                }
            }
        } else {
            for(String l : favoriteNews) {
                News temp = news.get(l);
                if(temp != null) {
                    response.add(temp);
                }
            }
        }
        return response;
    }

    public void favoriteTriggered(String newsID) {

        if (isFavorites(newsID)) {
            favoriteNews.remove(newsID);
            Log.d("NewsManager", "favorite : dislike");
        } else {
            favoriteNews.add(newsID);
            Log.d("NewsManager","favorite: like");
        }

        writeFavPreference();
    }

    public void createNews(News currentNews) {

        String newsID = currentNews.getNewsID();

        if (isRead(newsID))
            return;

        Log.d("NewsManager", "Created: [Raw Data = ]" + currentNews);
        news.put(newsID, currentNews);
        historyNews.add(newsID);

        writeHisPreference();
        DBManager.add(currentNews);
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
        return historyNews.contains(newsID);
    }

    public static boolean isFavorites(String newsID) {
        return favoriteNews.contains(newsID);
    }

    public static NewsManager getInstance() {
        if (!read)
            initialize();
        return instance;
    }
}

