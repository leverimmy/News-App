package com.java.xiongzeen.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

public class GetNewsFeed {
    /**
     * 获取当前日期
     *
     * @return 当前日期，以 "yyyy-MM-dd" 格式返回
     */
    static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * 获取新闻列表的 URL
     *
     * @param size       新闻篇数
     * @param startDate  开始时间
     * @param words      关键词
     * @param categories 类型
     * @return 新闻列表的 URL
     */
    static String getNewsUrl(int size, String startDate, String[] words, String categories) {
        StringBuilder result = new StringBuilder();
        for (String word : words)
            result.append(word).append(",");
        result = new StringBuilder(result.substring(0, result.length() - 1));
        return "https://api2.newsminer.net/svc/news/queryNewsList" +
                "?size=" + size +
                "&startDate=" + startDate +
                "&endDate=" + getCurrentDate() +
                "&words=" + result +
                "&categories=" + categories;
    }

    /**
     * 筛选满足条件的新闻
     *
     * @param size       新闻篇数
     * @param startDate  开始时间
     * @param words      关键字列表
     * @param categories 类型
     * @return 满足条件的新闻列表
     */
    static NewsFeed[] getNewsFeeds(int size, String startDate, String[] words, String categories) {
        try {
            String url = getNewsUrl(size, startDate, words, categories);
            JSONObject response = getUrlResponse(url);
            NewsFeed[] results = new NewsFeed[response.getJSONArray("data").length()];
            for (int i = 0; i < results.length; i++) {
                results[i] = new NewsFeed(response.getJSONArray("data").getJSONObject(i));
            }
            return results;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回新闻列表的 JSON 对象
     *
     * @param url 新闻列表的 URL
     * @return 新闻列表的 JSON 对象
     */
    static JSONObject getUrlResponse(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return new JSONObject(builder.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }
}

class NewsFeed {

    /**
     * 本质上，一篇新闻数据是一个 {@code JSONObject}
     */
    private final JSONObject data;

    /**
     * 构造函数
     *
     * @param jsonObject 由 {@code jsonObject} 构建
     */
    public NewsFeed(JSONObject jsonObject) {
        data = jsonObject;
    }

    /**
     * 获取新闻的图片列表
     *
     * @return 一个 String[]，代表新闻的图片列表
     */
    public String[] images() {
        try {
            String list = list = data.getString("image");
            list = list.substring(1, list.length() - 1);
            return list.split(", ");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的标题
     *
     * @return 一个 String，代表新闻的标题
     */
    public String title() {
        try {
            return data.getString("title");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的来源
     *
     * @return 一个 String，代表新闻的来源
     */
    public String publisher() {
        try {
            return data.getString("publisher");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的发布时间
     *
     * @return 一个 String，代表新闻的标题
     */
    public String publishTime() {
        try {
            return data.getString("publishTime");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的正文
     *
     * @return 一个 String，代表新闻的正文
     */
    public String content() {
        try {
            return data.getString("content");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的视频列表
     *
     * @return 一个 String[]，代表新闻的视频列表
     */
    public String[] videos() {
        try {
            String list = list = data.getString("video");
            list = list.substring(1, list.length() - 1);
            return list.split(", ");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的关键词列表
     *
     * @return 一个 String[]，代表新闻的关键词列表
     */
    public String[] keywords() {
        try {
            JSONArray keywordPairs = data.getJSONArray("keywords");
            String[] results = new String[keywordPairs.length()];
            for (int i = 0; i < keywordPairs.length(); i++) {
                results[i] = keywordPairs.getJSONObject(i).getString("word");
            }
            return results;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取新闻的唯一编号
     *
     * @return 一个 String，代表新闻的唯一编号
     */
    public String newsID () {
        try {
            return data.getString("newsID");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重载 {@code toString} 函数
     *
     * @return 按照 {@code data} 的形式输出
     */
    @Override
    public String toString() {
        return data.toString();
    }
}
