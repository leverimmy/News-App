package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;
import com.java.xiongzeen.data.News;
import com.java.xiongzeen.service.NewsManager;
import com.java.xiongzeen.service.TaskRunner;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment {
    public static final int PAGE_SIZE = 15;
    public static final String LOG_TAG = NewsListFragment.class.getSimpleName();
    private final List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private EndlessRecyclerViewScrollListener listScrollListener;
    private Context context;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout listContainer;
    private int page = 1;
    private View mView = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NewsList", "onCreateView");

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_news_list, container, false);
            context = mView.getContext();

            listContainer = mView.findViewById(R.id.news_list_container);
            listContainer.setOnRefreshListener(this::reloadNews);

            recyclerView = mView.findViewById(R.id.news_list);

            LinearLayoutManager llm = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(llm);

            listScrollListener = new EndlessRecyclerViewScrollListener(llm, (page, totalItemsCount, view1) -> loadNextPage());
            recyclerView.addOnScrollListener(listScrollListener);

            listAdapter = new NewsListAdapter(this, context, newsList);
            recyclerView.setAdapter(listAdapter);

            loadingBar = mView.findViewById(R.id.loading_bar);
            loadingBar.setVisibility(View.VISIBLE);


            reloadNews();
        } else {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null) {
                group.removeView(mView);
            }
        }

        return mView;
    }

    private void loadNextPage() { //这个函数来自2022年科协暑培的代码
        page += 1;
        Log.d("NewsListFragment","loadNextPage()" + page);
        loadingBar.setVisibility(View.VISIBLE);
        NewsManager.getInstance().newsList(PAGE_SIZE * (page - 1), PAGE_SIZE, new NewsFetchCallback(false));
    }

    public void reloadNews() { //这个函数来自2022年科协暑培的代码
        page = 1;
        if (listScrollListener == null)
            return;
        Log.d("NewsListFragment","reloadNews()");
        listScrollListener.resetState();
        NewsManager.getInstance().newsList(0, PAGE_SIZE, new NewsFetchCallback(true));
    }

    public class NewsFetchCallback implements TaskRunner.Callback<List<News>> { //这个函数来自2022年科协暑培的代码
        private final boolean reload;

        public NewsFetchCallback(boolean reload) { //这个函数来自2022年科协暑培的代码
            this.reload = reload;
        }

        @Override
        public void complete(TaskRunner.Result<List<News>> res) { //这个函数来自2022年科协暑培的代码
            loadingBar.setVisibility(View.GONE);
            if (reload) {
                listContainer.setRefreshing(false);
            }
            if (res.isOk()) {
                if (reload) {
                    newsList.clear();
                }
                newsList.addAll(res.getResult());
                listAdapter.notifyDataSetChanged();
                Log.d(LOG_TAG, "Post fetch succeeded, reload=" + reload);
                for (News i : newsList)
                    Log.d(LOG_TAG, i.toString());

            } else {
                Log.e(LOG_TAG, "Post fetch failed due to exception", res.getError());
                Toast.makeText(context, "没有网络连接，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
