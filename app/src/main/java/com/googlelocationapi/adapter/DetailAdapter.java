package com.googlelocationapi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.googlelocationapi.Data.ParcelablesData;
import com.googlelocationapi.Data.Results;
import com.googlelocationapi.R;
import com.googlelocationapi.activities.EachItemActivity;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailDataHolder>{

    Results results;
    ArrayList<ParcelablesData> parcelablesDataArrayList;
    Context context;

    public DetailAdapter(ArrayList<ParcelablesData> parcelablesDataArrayList, Context context){
        this.parcelablesDataArrayList = parcelablesDataArrayList;
        this.context = context;
    }
    @Override
    public DetailAdapter.DetailDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_adapter, parent, false);
        return new DetailDataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailAdapter.DetailDataHolder holder, int position) {

        ParcelablesData data = parcelablesDataArrayList.get(0);
        ArrayList<Results> resultList = data.getResultList();
            results = resultList.get(position);
            holder.name.setText(results.getName());
            holder.longitudeValue.setText(String.valueOf(results.getGeometry().getLocation().getLat()));
            holder.latitudeValue.setText(String.valueOf(results.getGeometry().getLocation().getLng()));
            holder.address.setText(results.getFormatted_address());
            holder.rating.setRating(results.getRating());
            Picasso.with(context).load(results.getIcon()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return parcelablesDataArrayList.get(0).getResultList().size();
    }

// ******************************************** Inner Class Started ****************************************************//

    public class DetailDataHolder extends RecyclerView.ViewHolder {

        TextView  latitudeValue, longitudeValue, address, name;
        RatingBar rating;
        ImageView imageView;

        public DetailDataHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            latitudeValue = (TextView) view.findViewById(R.id.latitudeValue);
            longitudeValue = (TextView) view.findViewById(R.id.longitudeValue);
            address = (TextView) view.findViewById(R.id.address);
            rating = (RatingBar) view.findViewById(R.id.ratingBar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = getAdapterPosition();

                    ParcelablesData data = parcelablesDataArrayList.get(0);
                    ArrayList<Results> resultList = data.getResultList();
                    results = resultList.get(i);
                    Intent intent = new Intent(context, EachItemActivity.class);
                    intent.putExtra("icon", results.getIcon());
                    intent.putExtra("address", results.getFormatted_address());
                    intent.putExtra("name", results.getName());
                    intent.putExtra("latitude", results.getGeometry().getLocation().getLat());
                    intent.putExtra("longitude", results.getGeometry().getLocation().getLng());
                    intent.putExtra("rating", results.getRating());
                    context.startActivity(intent);
                }
            });
        }
    }
    // ******************************************** Inner Class Ends ****************************************************//
}
