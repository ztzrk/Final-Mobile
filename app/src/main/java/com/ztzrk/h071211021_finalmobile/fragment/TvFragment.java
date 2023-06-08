package com.ztzrk.h071211021_finalmobile.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztzrk.h071211021_finalmobile.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TvFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TvFragment extends Fragment {

    private static TvFragment instance;

    public TvFragment() {
        // Required empty public constructor
    }

    public static TvFragment getInstance() {
        if (instance == null) {
            instance = new TvFragment();
        }
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv, container, false);
    }
}