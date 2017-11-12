package cgodin.qc.ca.androidrestaurant.activities;

import android.content.ClipData;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cgodin.qc.ca.androidrestaurant.R;

/**
 * Created by Ariel S on 2017-11-06.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnClickListener {

    private String[] mDataset;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private View view;


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);
        //String item = mList.get(itemPosition);
        String item = mDataset[itemPosition];
        //Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
        Toast.makeText(mContext, item, Toast.LENGTH_SHORT).show();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView mTextView;
        public MyViewHolder(View view){
            super(view);

            mCardView = (CardView) view.findViewById(R.id.card_view);
            mTextView = (TextView) view.findViewById(R.id.tv_text);

        }

    }


    public MyAdapter(String[] myDataset){
        mDataset = myDataset;

    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        mContext = parent.getContext();
        this.view = view;
        view.setOnClickListener(this);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        holder.mTextView.setText(mDataset[position]);

    }



    @Override
    public int getItemCount() { return mDataset.length; }


}
