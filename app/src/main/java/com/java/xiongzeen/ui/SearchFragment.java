package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;
import com.java.xiongzeen.service.FetchFromAPIManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private GridLayout gridLayout;
    private DatePicker startDatePicker, endDatePicker;
    private String queryText = "";
    private OnSearchInputFinished mListener;

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

    private void collectInformation() {

        MyApplication.searchPage = false;
        MyApplication.resultPage = true;

        Log.d("SearchFragment", queryText);
        List<String> categories = new ArrayList<>();
        int k = gridLayout.getChildCount();
        for(int i = 0; i < k; i++){
            CheckBox checkBox = (CheckBox) gridLayout.getChildAt(i);
            if(checkBox.isChecked()) {
                categories.add((String)checkBox.getText());
            }
        }

        int startTimeRaw = startDatePicker.getYear() * 10000
                + startDatePicker.getMonth() * 100
                + startDatePicker.getDayOfMonth();
        int endTimeRaw = endDatePicker.getYear() * 10000
                + endDatePicker.getMonth() * 100
                + endDatePicker.getDayOfMonth();

        if (endTimeRaw <= startTimeRaw) {
            Toast.makeText(getContext(), "开始时间需要早于结束时间！", Toast.LENGTH_SHORT).show();
            return;
        }

        String startTime = startDatePicker.getYear()
                + "-" +
                (startDatePicker.getMonth() + 1 <= 9
                        ? "0" + (startDatePicker.getMonth() + 1)
                        : (startDatePicker.getMonth() + 1))
                + "-" +
                (startDatePicker.getDayOfMonth() + 1 <= 9
                        ? "0" + startDatePicker.getDayOfMonth()
                        : startDatePicker.getDayOfMonth());

        String endTime = endDatePicker.getYear()
                + "-" +
                (endDatePicker.getMonth() + 1 <= 9
                        ? "0" + (endDatePicker.getMonth() + 1)
                        : (endDatePicker.getMonth() + 1))
                + "-" +
                (endDatePicker.getDayOfMonth() <= 9
                        ? "0" + endDatePicker.getDayOfMonth()
                        : endDatePicker.getDayOfMonth());

        Log.d("SearchFragment", queryText + categories + startTime + endTime);
        FetchFromAPIManager.getInstance().handleSearch(categories, startTime, endTime, queryText);
        mListener.finished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = view.findViewById(R.id.searchBar);
        gridLayout = view.findViewById(R.id.selections);
        startDatePicker = view.findViewById(R.id.datePicker1);
        endDatePicker = view.findViewById(R.id.datePicker2);

        endDatePicker.setMaxDate(new Date().getTime());
        startDatePicker.setMaxDate(new Date().getTime());

        Button searchButton = view.findViewById(R.id.search_button);
        searchView.setOnQueryTextListener(this);
        searchButton.setOnClickListener(view1 -> collectInformation());

        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        queryText = s;
        Log.d("SearchFragment", "onQueryTextSubmit:" + s);
        collectInformation();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        queryText = s;
        Log.d("SearchFragment", "onQueryTextChange:" + s);
        return false;
    }
}