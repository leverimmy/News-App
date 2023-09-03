package com.java.xiongzeen.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;
import com.java.xiongzeen.data.News;
import com.java.xiongzeen.service.NewsManager;
import com.java.xiongzeen.service.PictureLoader;

import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private final List<News> newsList;
    private final Fragment fragment;
    private final LayoutInflater inflater;
    private final Context mainActivity;


    public NewsListAdapter(Fragment fragment, Context context, List<News> newsList) {
        this.newsList = newsList;
        this.fragment = fragment;
        this.inflater = LayoutInflater.from(context);
        this.mainActivity =  MyApplication.getContext();

    }
    
    @Override
    public int getItemViewType(int position) {
        return newsList.get(position).getImages().length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //这个函数来自2022年科协暑培的代码
        View itemView;
        if(viewType == 0) {
            itemView = inflater.inflate(R.layout.news_title_no_image, parent, false);
        } else if(viewType == 1) {
            itemView = inflater.inflate(R.layout.news_title_one_image, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.news_title_two_image, parent, false);
        }
        return new ViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { //这个函数来自2022年科协暑培的代码
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private ImageView picture, picture2;
        private final int type;
        public ViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;

            if (type == 0) {
                title = itemView.findViewById(R.id.news_no_pic_title);
                description = itemView.findViewById(R.id.news_no_pic_description);
            } else if (type == 1) {
                title = itemView.findViewById(R.id.news_one_pic_title);
                description = itemView.findViewById(R.id.news_one_pic_description);
                picture = itemView.findViewById(R.id.news_one_pic_picture_0);
            } else {
                title = itemView.findViewById(R.id.news_two_pic_title);
                description = itemView.findViewById(R.id.news_two_pic_description);
                picture = itemView.findViewById(R.id.news_two_pic_picture_0);
                picture2 = itemView.findViewById(R.id.news_two_pic_picture_1);
            }
        }

        public void bindData(int position) {
            News news = newsList.get(position);
            String title = news.getTitle();

            if (NewsManager.isRead(news.getNewsID()) && (MyApplication.newsPage || MyApplication.searchPage))
                this.title.setTextColor(Color.GRAY);
            this.title.setText(title);

            this.description.setText(news.getPublisher() + " " + news.getPublishTime());
            Log.d("NewsListAdapter", "Making" + title);

            if(type != 0) {
                PictureLoader.loadPictureWithPlaceHolder(mainActivity, news.getImages()[0], picture);
            }
            if(type >= 2) {
                PictureLoader.loadPictureWithPlaceHolder(mainActivity, news.getImages()[1], picture2);
            }
            itemView.setOnClickListener(v -> {

                NewsManager.getInstance().createNews(news);
                notifyItemChanged(position);
                // download picture
                Bundle bundle = new Bundle();
                bundle.putString("newsID", news.getNewsID());
                Utils.replaceFragment(fragment, NewsDetailFragment.class, bundle);
            });
        }

    }

}
