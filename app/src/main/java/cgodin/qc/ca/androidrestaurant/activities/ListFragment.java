package cgodin.qc.ca.androidrestaurant.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cgodin.qc.ca.androidrestaurant.R;

/**
 * Created by Ariel S on 2017-11-06.
 */

public class ListFragment extends Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        RecyclerView rv = rootView.findViewById(R.id.rv_recycler_view);
        rv.setHasFixedSize(true);

        MyAdapter adapter = new MyAdapter(new String[]{"Restaurant 1", "Restaurant 2", "Restaurant 3", "Restaurant 4", "Restaurant 5" , "Restaurant 6" , "Restaurant 7", "Restaurant 8", "Restaurant 9", "Restaurant 10"});

        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}