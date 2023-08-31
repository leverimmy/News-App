package com.java.xiongzeen.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.java.xiongzeen.R;


public class VideoFragment extends Fragment {

    private String videoWebPath = "";


    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String path) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("video", path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            videoWebPath = getArguments().getString("video");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        try {
            VideoView videoView = view.findViewById(R.id.videoView);
            Uri uri = Uri.parse(videoWebPath);

            videoView.setVideoURI(uri);
            videoView.setMediaController(new MediaController(getContext()));
            videoView.requestFocus();
            videoView.setOnCompletionListener(mp -> {

            });
            videoView.setOnErrorListener((mp, what, extra) -> false);
            videoView.start();

            Log.d("VideoFragment", "isPlaying: " + videoView.isPlaying());
        } catch (Exception e) {
            Log.d("VideoFragment", "failed" );
        }

        return view;
    }
}