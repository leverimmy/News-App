package com.java.xiongzeen.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.java.xiongzeen.MyApplication;
import com.java.xiongzeen.R;
import com.java.xiongzeen.data.Category;

import java.util.ArrayList;
import java.util.List;

public class SelectPaddleFragment extends Fragment {

    private List<Category> selected = new ArrayList<>();
    private List<Category> unselected = new ArrayList<>();

    private SelectPaddleAdapter selected_adapter;
    private SelectPaddleAdapter unselected_adapter;

    private SelectPaddleAdapter.Interface listener;

    private onSelectPaddleListener mListener;


    public SelectPaddleFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listener = ((position, currentStatus) -> {
            if(currentStatus) {
                unselected.add(selected.remove(position));
            } else {
                selected.add(unselected.remove(position));
            }
            selected_adapter.notifyDataSetChanged();
            unselected_adapter.notifyDataSetChanged();
            mListener.reportCurrent(selected, unselected);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void upload() {
        MyApplication.myUser.selected = selected;
        MyApplication.myUser.unselected = unselected;
        MyApplication.myUser.writeSelectPreference();
    }

    public interface onSelectPaddleListener {
        void reportCurrent(List<Category> selected, List<Category> unselected);
        void selectPaddleConfirmed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onSelectPaddleListener) {
            mListener = (onSelectPaddleListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selected = MyApplication.myUser.selected;
        unselected = MyApplication.myUser.unselected;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_paddle, container, false);
        GridView selected_grid_view = view.findViewById(R.id.selected);
        GridView unselected_grid_view = view.findViewById(R.id.unselected);

        selected_adapter = new SelectPaddleAdapter(listener, selected, this.getContext(), true);
        unselected_adapter = new SelectPaddleAdapter(listener, unselected, this.getContext(), false);
        selected_grid_view.setAdapter(selected_adapter);
        unselected_grid_view.setAdapter(unselected_adapter);
        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            upload();
            mListener.selectPaddleConfirmed();
        });

        return view;
    }
}