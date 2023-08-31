package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.R;
import com.java.xiongzeen.service.FetchFromAPIManager;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private GridLayout gridLayout;
    private EditText startTime, endTime;
    private String queryText = "";
    private OnSearchInputFinished mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    public interface OnSearchInputFinished {
        void finished();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchInputFinished) {
            mListener = (OnSearchInputFinished) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void collectInformation() {
        Log.d("SearchFragment", queryText );
        List<String> categories = new ArrayList<>();
        int k = gridLayout.getChildCount();
        for(int i = 0; i < k; i++){
            CheckBox a = (CheckBox)gridLayout.getChildAt(i);
            if(a.isChecked()){
                categories.add((String)a.getText());
            }
        }

        String rawStartTime = startTime.getText().toString();
        String rawEndTime = endTime.getText().toString();

        String begin_time, end_time;

        if (rawStartTime.length() == 0)
            begin_time = "";
        else {
            begin_time = rawStartTime.substring(0, 4) + "-" +
                    rawStartTime.substring(4, 6) + "-" +
                    rawStartTime.substring(6);
        }
        if (rawEndTime.length() == 0)
            end_time = "";
        else {
            end_time = rawEndTime.substring(0, 4) + "-" +
                    rawEndTime.substring(4, 6) + "-" +
                    rawEndTime.substring(6);
        }

        Log.d("SearchFragment", queryText + categories + begin_time + end_time);
        FetchFromAPIManager.getInstance().handleSearch(categories, begin_time, end_time, queryText);
        mListener.finished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        SearchView searchView = view.findViewById(R.id.searchBar);
        gridLayout = view.findViewById(R.id.selections);
        startTime = view.findViewById(R.id.editTextDateStart);
        endTime = view.findViewById(R.id.editTextDateEnd);
        Button searchButton = view.findViewById(R.id.search_button);
        searchView.setOnQueryTextListener(this);
        searchButton.setOnClickListener(view1 -> collectInformation());

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        queryText = s;
        Log.d("onQueryTextSubmit", s);
        collectInformation();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        queryText = s;
        Log.d("onQueryTextChange", s);
        return false;
    }
}