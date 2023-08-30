package com.java.xiongzeen.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.data.News;
import com.java.xiongzeen.ui.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NewsManager {
    private final static NewsManager instance = new NewsManager();
    private static Map<Long, News> news = new HashMap<>();
    private static Map<String, Long> id_convert = new HashMap<>();
    private static Map<Long, String> id_re_convert = new HashMap<>();
    private static List<Long> historyNews = new ArrayList<>();
    private static List<Long> favoriteNews = new ArrayList<>();
    private static boolean read = false;

    public static void writeFavPreference() {
        Log.d("preferenceTest","fav");
        SharedPreferences preferences_fav = MyApplication.getContext().getSharedPreferences("fav",0);

        preferences_fav.edit().putString("fav", Utils.listToString(favoriteNews)).apply();

    }
    public static void writeHisPreference() {
        Log.d("preferenceTest","his");
        SharedPreferences preferences_his = MyApplication.getContext().getSharedPreferences("his",0);

        preferences_his.edit().putString("his", Utils.listToString(historyNews)).apply();

    }

    private static void readNewsFromPreference() {
        Log.d("preferenceTest","read");
        SharedPreferences preferences_his = MyApplication.getContext().getSharedPreferences("his",0);
        SharedPreferences preferences_fav = MyApplication.getContext().getSharedPreferences("fav",0);

        historyNews = Utils.stringToList(preferences_his.getString("his",""));
        favoriteNews = Utils.stringToList(preferences_fav.getString("fav",""));

        Log.d("preferenceTest","read" + historyNews.size() + " " + favoriteNews.size());

        for(long a : favoriteNews) {
            news.get(a).setFavorites(true);
        }
    }

    public static void read_from_disk() {
        List<News> temp = DBManager.query();
        for(News item : temp){
            long id = item.getId();
            news.put(id, item);
            id_convert.put(item.getNewsID(),id);
            id_re_convert.put(id,item.getNewsID());
        }
        readNewsFromPreference();
        read = true;
    }

    public static long convert_id(String newsID) {
        if(!read)
            read_from_disk();
        if(id_convert.containsKey(newsID)) {
            return id_convert.get(newsID);
        } else {
            return -1L;
        }
    }

    public List<News> get_record(boolean mode) { // 0 for history, 1 for favorite
        if(!read)
            read_from_disk();
        Log.d("NewsManager", "Trying to get news");
        List<News> response = new ArrayList<>();
        if(!mode) {
            for(long l : historyNews) {
                News temp = news.get(l);
                if(temp != null) {
                    response.add(temp);
                }
            }
        } else {
            for(long l : favoriteNews) {
                News temp = news.get(l);
                if(temp != null) {
                    response.add(temp);
                }
            }
        }
        return response;
    }

    public void favorite_triggered(Long id, boolean like) {
        if(!read)
            read_from_disk();
        News operating = news.get(id);
        if (operating == null)
            return;
        if (!operating.isFavorites()) {
            if(like) {
                operating.setFavorites(true);
                favoriteNews.add(id);
                Log.d("favourite","like");
            }
        } else {
            if (!like) {
                operating.setFavorites(false);
                favoriteNews.remove(id);
                Log.d("favourite", "dislike");
            }
        }
        writeFavPreference();
    }

    public Long createNews(News a_temp_news){
        if(!read)
            read_from_disk();
        if(id_convert.containsKey(a_temp_news.getNewsID())){
            Long id_ = id_convert.get(a_temp_news.getNewsID());
            historyNews.add(id_);
            historyNews.remove(id_);
            writeHisPreference();
            return id_;
        }
        long id = news.size();


        News a_new_one  = a_temp_news;
        a_new_one.setId(id);
        a_new_one.setBeenRead(true);
        Log.d("NewsManager::_createNews(from temp news)", a_new_one.toString());
        news.put(id,a_new_one);
        id_convert.put(a_new_one.getNewsID(),id);
        id_re_convert.put(id, a_new_one.getNewsID());
        historyNews.add(id);
        writeHisPreference();
        DBManager.add(news);

        return id;
    }

    public News getNews(long id) {
        if(!read)
            read_from_disk();
        return news.get(id);
    }

    public void newsList(int offset, int pageSize, TaskRunner.Callback<List<News>> callback) {
        if(!read)
            read_from_disk();
        TaskRunner.getInstance().execute(() -> applyForNews(offset, pageSize), callback);
    }

    private List<News> applyForNews(int offset, int pageSize) {
        if(!read)
            read_from_disk();
        return FetchFromAPIManager.getInstance().getNews(offset, pageSize);
    }

    public static NewsManager getInstance() {
        return instance;
    }

}

