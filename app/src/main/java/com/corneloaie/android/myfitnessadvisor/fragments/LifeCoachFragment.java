package com.corneloaie.android.myfitnessadvisor.fragments;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.corneloaie.android.myfitnessadvisor.R;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

public class LifeCoachFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifecoach, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.lifecoach_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        //TODO update videos IDs
        String[] videoIds = {"XBkQ3jH2jUQ", "xZAGmP3cxtg",
                "Q4yUlJV31Rk", "EByF_9CmMag"};
        RecyclerView.Adapter recyclerViewAdapter = new RecyclerViewAdapter(videoIds, this.getLifecycle());
        recyclerView.setAdapter(recyclerViewAdapter);


        return view;
    }


    private class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
        private String[] videoIds;
        private Lifecycle lifecycle;

        RecyclerViewAdapter(String[] videoIds, Lifecycle lifecycle) {
            this.videoIds = videoIds;
            this.lifecycle = lifecycle;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            YouTubePlayerView youTubePlayerView = (YouTubePlayerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_item, parent, false);
            youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
            lifecycle.addObserver(youTubePlayerView);

            return new ViewHolder(youTubePlayerView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.cueVideo(videoIds[position]);

        }

        @Override
        public int getItemCount() {
            return videoIds.length;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private YouTubePlayerView youTubePlayerView;
        private YouTubePlayer youTubePlayer;
        private String currentVideoId;


        ViewHolder(YouTubePlayerView playerView) {
            super(playerView);
            youTubePlayerView = playerView;

            youTubePlayerView.initialize(initializedYouTubePlayer ->
                    initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            youTubePlayer = initializedYouTubePlayer;
                            youTubePlayer.cueVideo(currentVideoId, 0);
                        }
                    }), true
            );
        }

        void cueVideo(String videoId) {
            currentVideoId = videoId;

            if (youTubePlayer == null)
                return;

            youTubePlayer.cueVideo(videoId, 0);
        }
    }


}
