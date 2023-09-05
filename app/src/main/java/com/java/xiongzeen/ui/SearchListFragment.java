package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class SearchListFragment extends Fragment {
    public static final int PAGE_SIZE = 15;
    private List<News> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsListAdapter listAdapter;
    private EndlessRecyclerViewScrollListener listScrollListener;
    private Context context;
    private ProgressBar loadingBar;
    private SwipeRefreshLayout listContainer;
    private int page = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("SearchListFragment", "onCreateView");

        if (MyApplication.SearchList != null) {
            Log.d("SearchListFragment", "MyApplication.SearchList != null");

            ViewGroup originalParent = (ViewGroup) MyApplication.SearchList.getParent();
            if (originalParent != null)
                originalParent.removeView(MyApplication.SearchList);

            return MyApplication.SearchList;
        }


        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        context = view.getContext();

        listContainer = view.findViewById(R.id.news_list_container);
        listContainer.setOnRefreshListener(this::reloadNews);

        recyclerView = view.findViewById(R.id.news_list);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        listScrollListener = new EndlessRecyclerViewScrollListener(llm, (page, totalItemsCount, view1) -> loadNextPage());
        recyclerView.addOnScrollListener(listScrollListener);

        listAdapter = new NewsListAdapter(this, context, newsList);

        recyclerView.setAdapter(listAdapter);

        loadingBar = view.findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);

        reloadNews();

        MyApplication.SearchList = view;

        return view;
    }

    private void loadNextPage() { //这个函数来自2022年科协暑培的代码
        page += 1;
        Log.d("NewsListFragment","loadNextPage()" + page);
        loadingBar.setVisibility(View.VISIBLE);
        NewsManager.getInstance().newsList(PAGE_SIZE * (page-1), PAGE_SIZE, new NewsFetchCallback(false));
    }

    public void reloadNews() { //这个函数来自2022年科协暑培的代码
        page = 1;
        if (listScrollListener == null)
            return;
        Log.d("NewsListFragment","reloadNews()");
        listScrollListener.resetState();
        NewsManager.getInstance().newsList(0, PAGE_SIZE, new NewsFetchCallback(true));
        Log.d("NewsListFragment","reloadNews() finished");
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
                if (newsList.isEmpty())
                    Toast.makeText(context, "无搜索结果！", Toast.LENGTH_SHORT).show();

                listAdapter.notifyDataSetChanged();
            } else {
                Log.e("NewsListFragment", "Post fetch failed due to exception", res.getError());
            }
        }
    }

}
