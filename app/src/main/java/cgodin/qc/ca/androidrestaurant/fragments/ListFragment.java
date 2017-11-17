package cgodin.qc.ca.androidrestaurant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.adapters.MyAdapter;
import cgodin.qc.ca.androidrestaurant.model.Restaurant;
import cgodin.qc.ca.androidrestaurant.utilities.JSONGetRequest;

/**
 * Created by Ariel S on 2017-11-06.
 */

public class ListFragment extends Fragment {


    private ArrayList<Restaurant> lstRestaurants = null;
    public MyAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        lstRestaurants = JSONGetRequest.lstRestaurants;

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView rv = rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        adapter = new MyAdapter(lstRestaurants);

        rv.setAdapter(adapter);
        rv.setVisibility(View.VISIBLE);

        adapter.notifyDataSetChanged();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        Log.e("onCreateView:", "adapter..");

        return rootView;
    }

}