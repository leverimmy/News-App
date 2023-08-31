package com.java.xiongzeen.ui;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private final int startingPageIndex = 0;
    private final OnLoadMoreListener onLoadMore;

    RecyclerView.LayoutManager mLayoutManager;

    @FunctionalInterface
    public interface OnLoadMoreListener { //这个函数来自2022年科协暑培的代码
        void onLoadMore(int page, int totalItemsCount, RecyclerView view);
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager, OnLoadMoreListener onLoadMore) { //这个函数来自2022年科协暑培的代码
        this.mLayoutManager = layoutManager;
        this.onLoadMore = onLoadMore;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager, OnLoadMoreListener onLoadMore) { //这个函数来自2022年科协暑培的代码
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.onLoadMore = onLoadMore;
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager, OnLoadMoreListener onLoadMore) { //这个函数来自2022年科协暑培的代码
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
        this.onLoadMore = onLoadMore;
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) { //这个函数来自2022年科协暑培的代码
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) { //这个函数部分来自2022年科协暑培的代码
       // Log.e("TAG",""+dx + "" + dy);
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) { //这个函数来自2022年科协暑培的代码
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) { //这个函数来自2022年科协暑培的代码
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) { //这个函数来自2022年科协暑培的代码
            currentPage++;
            onLoadMore.onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    // Call this method whenever performing new searches
    public void resetState() { //这个函数来自2022年科协暑培的代码
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }
}