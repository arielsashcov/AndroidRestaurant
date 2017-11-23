package cgodin.qc.ca.androidrestaurant.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import cgodin.qc.ca.androidrestaurant.R;

public class RestaurantDetailFragment extends Fragment {

    TextView tvName ;
    TextView tvAddress ;
    RatingBar listrating;
    ImageView imgRestaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        tvName = view.findViewById(R.id.tv_name);
        listrating = view.findViewById(R.id.listrating);
        tvAddress = view.findViewById(R.id.tv_address);
        imgRestaurant = view.findViewById(R.id.img_restaurant);


        //String name = getArguments().getString("name");
        //tvName.setText(name);

        final Bundle bdl = getArguments();

        try
        {
            tvName.setText(bdl.getString("name"));
        }
        catch(final Exception e)
        {
            Log.e("EXXXXXXXXXXXXXXX", e.toString());

        }

        return view;
    }

}
