package cgodin.qc.ca.androidrestaurant.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;



import com.google.android.gms.location.places.*;
import android.os.Bundle;

import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.R;

/**
 * Created by Ariel S on 2017-11-06.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] mDataset;
    private RecyclerView mRecyclerView;
    private Context mContext;




    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView mTextView;
        public MyViewHolder(View view){
            super(view);

            mCardView = (CardView) view.findViewById(R.id.card_view);
            mTextView = (TextView) view.findViewById(R.id.tv_name);

        }

    }


    //Constructor: Later change myDataset to actual Json from API
    public MyAdapter(String[] myDataset){
        mDataset = myDataset;

    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        mContext = parent.getContext();
        return new MyViewHolder(view);
    }


    //Associate data to each row
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        final String element = mDataset[position];

        holder.mTextView.setText(element);

        holder.mCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Click on : " + element, Toast.LENGTH_SHORT).show();
            }
        });

    }





    @Override
    public int getItemCount() { return mDataset.length; }


}
