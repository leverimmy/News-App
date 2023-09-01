package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;
import com.java.xiongzeen.data.News;
import com.java.xiongzeen.service.NewsManager;
import com.java.xiongzeen.service.PictureLoader;


public class NewsDetailFragment extends Fragment {
    private Context context;
    private ImageView news_image, news_image2, news_image3, news_image4;
    private String newsID = null;
    private FloatingActionButton unfavoriteButton, favoriteButton;
    private View mView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.reading_news_one_pic, container, false);
        } else {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null)
                group.removeView(mView);
        }

        if (getArguments() != null)
            newsID = getArguments().getString("newsID",null);
        News newsToShow = (newsID == null) ?
                new News() : NewsManager.getInstance().getNews(newsID);

        context = mView.getContext();

        TextView news_title = mView.findViewById(R.id.detail_title);
        TextView news_description = mView.findViewById(R.id.detail_description);
        TextView news_content = mView.findViewById(R.id.detail_content);
        news_image = mView.findViewById(R.id.detail_image1);
        news_image2 = mView.findViewById(R.id.detail_image2);
        news_image3 = mView.findViewById(R.id.detail_image3);
        news_image4 = mView.findViewById(R.id.detail_image4);
        FragmentContainerView containerView = mView.findViewById(R.id.fragment_to_contain_video);

        unfavoriteButton = mView.findViewById(R.id.favoriteFloatingActionButton);
        favoriteButton = mView.findViewById(R.id.favoriteFloatingActionButton2);
        Log.d("NewsDetailFragment","open_a_news: " + newsToShow.getNewsID());
        news_title.setText(newsToShow.getTitle());
        news_description.setText(newsToShow.getPublisher() + "     " + newsToShow.getPublishTime());
        news_content.setText(newsToShow.getContent());

        unfavoriteButton.setOnClickListener(v-> handle_favorite_click(true));
        favoriteButton.setOnClickListener(v-> handle_favorite_click(false));

        if (NewsManager.isFavorites(newsID)) {
            unfavoriteButton.setVisibility(View.GONE);
        } else {
            favoriteButton.setVisibility(View.GONE);
        }

        if (newsToShow.getImages().length >= 1) {
            PictureLoader.loadPictureWithoutPlaceHolder(context, newsToShow.getImages()[0],news_image);
        } else {
            news_image.setVisibility(View.GONE);
        }
        if (newsToShow.getImages().length >= 2) {
            PictureLoader.loadPictureWithoutPlaceHolder(context, newsToShow.getImages()[1],news_image2);
        } else {
            news_image2.setVisibility(View.GONE);
        }
        if (newsToShow.getImages().length >= 3) {
            PictureLoader.loadPictureWithoutPlaceHolder(context, newsToShow.getImages()[2],news_image3);
        } else {
            news_image3.setVisibility(View.GONE);
        }
        if (newsToShow.getImages().length >= 4) {
            PictureLoader.loadPictureWithoutPlaceHolder(context, newsToShow.getImages()[3],news_image4);
        } else {
            news_image4.setVisibility(View.GONE);
        }
        if (newsToShow.getVideo() != null && newsToShow.getVideo().length() != 0) {
            String path = newsToShow.getVideo();
            VideoFragment to_fill = VideoFragment.newInstance(path);
            getParentFragmentManager().beginTransaction().add(R.id.fragment_to_contain_video, to_fill).commit();
        } else {
            containerView.setVisibility(View.GONE);
        }

        Log.d("detailsPage", "true");


        if (MyApplication.newsPage) {

            MyApplication.newsPage = false;
            MyApplication.detailsPageFromNews = true;

        } else if (MyApplication.searchPage) {

            MyApplication.searchPage = false;
            MyApplication.detailsPageFromSearch = true;

        } else if (MyApplication.historyPage) {

            MyApplication.historyPage = false;
            MyApplication.detailsPageFromHistory = true;

        } else if (MyApplication.favoritePage) {

            MyApplication.favoritePage = false;
            MyApplication.detailsPageFromFavorite = true;

        }

        MyApplication.getBottomNavigationView().setVisibility(View.GONE);
        MyApplication.getTopFragmentContainer().setVisibility(View.GONE);

        return mView;
    }

    @Override
    public void onStop() {

        super.onStop();

        Log.d("detailsPage", "false");
        MyApplication.getBottomNavigationView().setVisibility(View.VISIBLE);
        if (MyApplication.newsPage)
            MyApplication.getTopFragmentContainer().setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {

        super.onResume();

        Log.d("detailsPage", "true");
        MyApplication.getBottomNavigationView().setVisibility(View.GONE);
        MyApplication.getTopFragmentContainer().setVisibility(View.GONE);

    }

    private void handle_favorite_click(boolean isFavorite) {
        if(isFavorite) {
            NewsManager.getInstance().favorite_triggered(newsID);
            Toast.makeText(context, "添加收藏成功！", Toast.LENGTH_SHORT).show();
            favoriteButton.setVisibility(View.VISIBLE);
            unfavoriteButton.setVisibility(View.GONE);
        } else {
            NewsManager.getInstance().favorite_triggered(newsID);
            Toast.makeText(context, "取消收藏成功！", Toast.LENGTH_SHORT).show();
            unfavoriteButton.setVisibility(View.VISIBLE);
            favoriteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        if (mView != null) {
            ViewGroup group = (ViewGroup) mView.getParent();

            if (group != null) {
                group.removeAllViews();
            }
        }
        super.onDestroyView();
    }
}
