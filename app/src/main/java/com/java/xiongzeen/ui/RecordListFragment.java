package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.xiongzeen.R;
import com.java.xiongzeen.service.NewsManager;

public class RecordListFragment extends Fragment {

    private boolean mode = false; // 0 for history, 1 for favorite
    private View mView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getBoolean("mode");
        }
        Log.d("record list", "mode" + mode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_record_list, container, false);
        } else {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null)
                group.removeView(mView);
        }

        Context context = mView.getContext();
        RecyclerView recyclerView = mView.findViewById(R.id.news_list);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        recyclerView.setLayoutManager(llm);
        NewsListAdapter listAdapter = new NewsListAdapter(this, context, NewsManager.getInstance().get_record(mode));
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        return mView;
    }
}
