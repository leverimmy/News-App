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
    private View mView = null;


    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String path) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("video", path);
        videoFragment.setArguments(args);
        return videoFragment;
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

        if (mView != null) {
            ViewGroup group = (ViewGroup) mView.getParent();
            if (group != null)
                group.removeView(mView);
        }

        mView = inflater.inflate(R.layout.fragment_video, container, false);

        try {
            VideoView videoView = mView.findViewById(R.id.videoView);
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

        return mView;
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