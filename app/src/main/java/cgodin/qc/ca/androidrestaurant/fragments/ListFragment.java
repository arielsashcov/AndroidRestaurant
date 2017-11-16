package cgodin.qc.ca.androidrestaurant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


    private ArrayList<Restaurant> lstRestaurants;

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

        MyAdapter adapter = new MyAdapter(lstRestaurants);

        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}