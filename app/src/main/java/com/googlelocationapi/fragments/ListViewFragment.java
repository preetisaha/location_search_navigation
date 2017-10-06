package com.googlelocationapi.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googlelocationapi.Data.ParcelablesData;
import com.googlelocationapi.R;
import com.googlelocationapi.adapter.DetailAdapter;

import java.util.ArrayList;

public class ListViewFragment extends Fragment {

    FragmentTransaction fragmentTransaction;
    RecyclerView recyclerView;
    View view;
    DetailAdapter adapter;
    ArrayList<ParcelablesData> parcelablesDataArrayList;

    public ListViewFragment() {}

    public static ListViewFragment newInstance(String param1, String param2) {
        ListViewFragment fragment = new ListViewFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parcelablesDataArrayList = getArguments().getParcelableArrayList("key");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_list_view, container, false);
        recyclerView = (RecyclerView)getActivity().findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.INVISIBLE);
        FloatingActionButton fab2 = (FloatingActionButton) getActivity().findViewById(R.id.fab2);

        RecyclerView recyclerViewFragment = (RecyclerView) view.findViewById(R.id.recyclerViewFragment);
        adapter = new DetailAdapter(parcelablesDataArrayList, getActivity());

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewFragment.setLayoutManager(mLayoutManager);
        recyclerViewFragment.setAdapter(adapter);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                GoogleMapFragment mapFragment = new GoogleMapFragment();
                FragmentManager frg=getFragmentManager();
                fragmentTransaction = frg.beginTransaction();
                fragmentTransaction.replace(R.id.mapContainer, mapFragment);
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {}
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
