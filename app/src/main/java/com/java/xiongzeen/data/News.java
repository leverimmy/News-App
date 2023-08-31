package com.java.xiongzeen.data;


import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class News {
    private String title = "Error";
    private String publisher = "";
    private String publishTime = "";
    private String content = "";
    private String[] images = null;
    private String video = "";
    private String newsID = "";
    private String url = "";
    private boolean isFavorites = false;
    private boolean beenRead = false;


    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getContent() {
        return content;
    }

    public String[] getImages() {
        return images;
    }

    public String getVideo() {
        return video;
    }

    public String getNewsID() {
        return newsID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        News that = (News) o;
        return newsID.equals(that.newsID);
    }

    @Override
    public int hashCode() {
        return Objects.hash("HashCode" + newsID);
    }

    public News() {

    }

    public News(JSONObject data) {
        try {
            title = data.getString("title");
            publisher = data.getString("publisher");
            publishTime = data.getString("publishTime");
            content = data.getString("content");

            String list = data.getString("image");
            list = list.substring(1, list.length() - 1);
            images = list.split(", ");

            video = data.getString("video");
            newsID = data.getString("newsID");
            url = data.getString("url");

            isFavorites = data.getBoolean("isFavorites");
            beenRead = data.getBoolean("beenRead");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("publisher", publisher);
            jsonObject.put("publishTime", publishTime);
            jsonObject.put("content", content);

            StringBuilder list = new StringBuilder();
            for (String image : images) {
                list.append(image).append(", ");
            }
            if (list.length() == 0)
                list = new StringBuilder("[]");
            else
                list = new StringBuilder("[" + list.substring(0, list.length() - 2) + "]");
            jsonObject.put("image", list.toString());

            jsonObject.put("video", video);
            jsonObject.put("newsID", newsID);
            jsonObject.put("url", url);
            jsonObject.put("isFavorites", isFavorites);
            jsonObject.put("beenRead", beenRead);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
