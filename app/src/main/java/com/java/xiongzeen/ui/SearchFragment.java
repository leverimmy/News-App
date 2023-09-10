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

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;
import com.java.xiongzeen.data.Page;
import com.java.xiongzeen.service.FetchFromAPIManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener{

    private GridLayout gridLayout;
    private DatePicker startDatePicker, endDatePicker;
    private SearchView searchView;
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

        MyApplication.page = Page.RESULT;

        Log.d("SearchFragment", queryText);
        List<String> categories = new ArrayList<>();
        int k = gridLayout.getChildCount();
        for(int i = 0; i < k; i++){
            CheckBox checkBox = (CheckBox) gridLayout.getChildAt(i);
            if(checkBox.isChecked()) {
                categories.add((String) checkBox.getText());
            }
        }

        Calendar startTime = Calendar.getInstance();
        startTime.set(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth());

        Calendar endTime = Calendar.getInstance();
        endTime.set(endDatePicker.getYear(), endDatePicker.getMonth(), endDatePicker.getDayOfMonth());

        endTime.add(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String startTimeString = dateFormat.format(startTime.getTime());
        String endTimeString = dateFormat.format(endTime.getTime());

        Log.d("SearchFragment", queryText + categories + startTimeString + endTimeString);
        FetchFromAPIManager.getInstance().handleSearch(categories, startTimeString, endTimeString, queryText);
        mListener.finished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchBar);

        searchView.setQueryHint("请输入搜索的关键词，多个关键词请用“,”（半角逗号）隔开");
        searchView.onActionViewExpanded();

        gridLayout = view.findViewById(R.id.selections);
        startDatePicker = view.findViewById(R.id.datePicker1);
        endDatePicker = view.findViewById(R.id.datePicker2);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        startDatePicker.setMaxDate(calendar.getTimeInMillis());
        startDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        endDatePicker.setMaxDate(calendar.getTimeInMillis());
        endDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        endDatePicker.init(endDatePicker.getYear(), endDatePicker.getMonth(), endDatePicker.getDayOfMonth(), (view12, year, monthOfYear, dayOfMonth) -> {

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth());

            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(year, monthOfYear, dayOfMonth);

            if (calendar1.compareTo(calendar2) > 0) {
                calendar1 = calendar2;
            }
            startDatePicker.setMaxDate(calendar2.getTimeInMillis());
            startDatePicker.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));

        });

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