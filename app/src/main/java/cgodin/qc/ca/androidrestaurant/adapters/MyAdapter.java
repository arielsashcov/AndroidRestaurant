package cgodin.qc.ca.androidrestaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cgodin.qc.ca.androidrestaurant.R;
import cgodin.qc.ca.androidrestaurant.activities.InfoMapActivity;
import cgodin.qc.ca.androidrestaurant.model.Restaurant;

/**
 * Created by Ariel S on 2017-11-06.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Restaurant> mDataset;
    private RecyclerView mRecyclerView;
    private Context mContext;


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;

        public TextView mTitle;
        public TextView mAddress;
        public ImageView mImageView;
        public RatingBar mRatingBar;


        public MyViewHolder(View view){
            super(view);

            mCardView = (CardView) view.findViewById(R.id.card_view);
            mTitle = (TextView) view.findViewById(R.id.tv_name);
            mAddress = (TextView) view.findViewById(R.id.tv_address);
            mImageView = (ImageView) view.findViewById(R.id.img_restaurant);
            mRatingBar = (RatingBar) view.findViewById(R.id.listrating);

        }
    }

    //Constructor: Later change myDataset to actual Json from API
    public MyAdapter(ArrayList<Restaurant> myDataset){
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
    public void onBindViewHolder(MyViewHolder holder, final int position){

        final String element = String.valueOf(mDataset.get(position).getName());

        holder.mTitle.setText(String.valueOf(mDataset.get(position).getName()));
        holder.mAddress.setText(String.valueOf(mDataset.get(position).getFormatted_address()));

        if (mDataset.get(position).getImg_link().isEmpty()) {
            holder.mImageView.setImageResource(R.drawable.noimage);
        } else{
            Picasso.with(mContext)
                    .load(mDataset.get(position).getImg_link())
                    .fit()
                    .centerCrop()
                    .into(holder.mImageView);
        }

        if((float) mDataset.get(position).getRating() == -1){
            holder.mRatingBar.setVisibility(View.INVISIBLE);
        }else {
            holder.mRatingBar.setRating((float) mDataset.get(position).getRating());
        }

        final Restaurant clickedRestaurant = mDataset.get(position);

        holder.mCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, InfoMapActivity.class);
                intent.putExtra("restaurant", (Parcelable) clickedRestaurant);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "Click on : " + element, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mCardView.invalidate();

    }

    @Override
    public int getItemCount() { return mDataset.size(); }




}
