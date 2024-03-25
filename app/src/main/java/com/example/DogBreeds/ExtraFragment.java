package com.example.DogBreeds;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;


public class ExtraFragment extends Fragment {
    RecyclerView recyclerView;
    List<youTubeVideos> youtubeVideos = new Vector<>();
    private Button btnPrev, btnNext;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view=inflater.inflate(R.layout.fragment_extra, container, false);
       //view.setContentView(R.layout.fragment_extra);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnPrev = view.findViewById(R.id.pre);
        btnNext = view.findViewById(R.id.next);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        youtubeVideos.add( new youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/JgeZRe8D0Ow\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","ধানের উফরা রোগ প্রতিরোধ এর উপায়"));
        youtubeVideos.add( new youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/YaIayb1NjQU\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>", "ধানের রোগ প্রতিরোধে কখন কি স্প্রে করবেন"));
        youtubeVideos.add( new youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/sLu09N7RkPk\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","কিভাবে ধানের পচন রোগ দমন করবেন " ) );
        youtubeVideos.add( new youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/DGxf40g0mPM\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"," ধানের ব্লাস্ট রোগ দমন ব্যবস্থা" ) );
        youtubeVideos.add( new youTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/LpE_7wZ1_FM\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","ধান গাছের ,খোলাপচা,গোড়া পচা, ঝলসা রোগ" ) );

       //aita
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        VideoAdapter videoAdapter = new VideoAdapter(getItemsForPage(currentPage));
        recyclerView.setAdapter(videoAdapter);


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 1) {
                    currentPage--;
                    videoAdapter.setItems(getItemsForPage(currentPage));
                    videoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "You are on the first page", Toast.LENGTH_SHORT).show();
                }
            }
        });





        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalPages = getTotalPages();
                if (currentPage < totalPages) {
                    currentPage++;
                    videoAdapter.setItems(getItemsForPage(currentPage));
                    videoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "You are on the last page", Toast.LENGTH_SHORT).show();
                }
            }
        });






        return view;
    }

    private List<youTubeVideos> getItemsForPage(int page) {
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, youtubeVideos.size());
        return youtubeVideos.subList(startIndex, endIndex);
    }


    private int getTotalPages() {
        return (int) Math.ceil((double) youtubeVideos.size() / PAGE_SIZE);
    }
}