/*
 * @author: David Ochoa Gutierrez
 * @contact: @theradikalstyle - david.ochoa.gtz@outlook.com
 * @copyright: TheRadikalSoftware - 2019
 */

package com.theradikalsoftware.examennexusarkus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterData extends RecyclerView.Adapter<AdapterData.ViewHolder> {
    ArrayList<PlacesData> dataset;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, address, distance, petFriendlyText;
        ImageView main, petfriendly;
        RatingBar rating;
        Context ctx;
        ArrayList<PlacesData> listData;

        public ViewHolder(View itemView, Context context, ArrayList<PlacesData> arrayL) {
            super(itemView);
            ctx = context;
            listData = arrayL;
            name = itemView.findViewById(R.id.adapter_textview_locationname);
            address = itemView.findViewById(R.id.adapter_textview_locationaddress);
            distance = itemView.findViewById(R.id.adapter_textview_locationdistance);
            main = itemView.findViewById(R.id.adapter_imageview_mainimage);
            petfriendly = itemView.findViewById(R.id.adapter_imageview_locationispetfriendly);
            petFriendlyText = itemView.findViewById(R.id.adapter_textview_petfriendlytext);
            rating = itemView.findViewById(R.id.adapter_ratingbar_locationrating);
            rating.setNumStars(5);
            rating.setMax(5);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("Adapter position ->", ""+getAdapterPosition());
            Log.d("Item ID position ->", ""+getItemId());
            Intent intent = new Intent(ctx, DetailPlaceActivity.class);
            intent.putExtra("adapPosition", getAdapterPosition());
            intent.putExtra("adapter", listData);
            ctx.startActivity(intent);
        }
    }

    public AdapterData(ArrayList<PlacesData> myDataset, Context mContext){
        dataset = myDataset;
        context = mContext;
        setHasStableIds(true);
    }


    @Override
    public AdapterData.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_view_list, parent, false);
        return new ViewHolder(itemView, context, dataset);
    }

    @Override
    public void onBindViewHolder(AdapterData.ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);
        Picasso.get().load(dataset.get(i).getThumbnail()).resize(50,50).centerInside().into(viewHolder.main); //Set PlacePicture
        viewHolder.name.setText(dataset.get(i).getPlaceName()); //Set PlaceName
        viewHolder.rating.setRating(Float.parseFloat(dataset.get(i).getRating())); //Set PlaceRating
        viewHolder.address.setText(dataset.get(i).getAddress()); //Set address
        viewHolder.distance.setText(String.format(context.getResources().getString(R.string.template_distance_between), dataset.get(i).getDistance())); //Set distance

        if(dataset.get(i).getIsPetFriendly().equals("true")){
            viewHolder.petfriendly.setVisibility(View.VISIBLE);
            viewHolder.petFriendlyText.setVisibility(View.VISIBLE);
            viewHolder.petfriendly.setImageDrawable(context.getResources().getDrawable(R.mipmap.dogfriendlyactive));
        }
    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
