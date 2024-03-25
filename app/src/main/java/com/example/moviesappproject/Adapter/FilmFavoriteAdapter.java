package com.example.moviesappproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.moviesappproject.Activity.DetailActivity;
import com.example.moviesappproject.Domain.Filmitem;
import com.example.moviesappproject.R;

import java.util.List;

public class FilmFavoriteAdapter extends RecyclerView.Adapter<FilmFavoriteAdapter.ViewHolder> {
    private List<Filmitem> items; // Change OMDBResponse to List<Search>
    private Context context;
    private  String userEmail;

    public FilmFavoriteAdapter(List<Filmitem> items,String useremail) {
        this.items = items;
        this.userEmail=useremail;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_film, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Filmitem filmitem = items.get(position); // Get the Search object at the current position
        holder.titleTxt.setText(filmitem.getTitle()); // Set the title from the Search object

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load(filmitem.getPoster()) // Load the poster URL from the Search object
                .apply(requestOptions)
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", filmitem.getImdbID()); // Pass the IMDB ID of the Search object
                intent.putExtra("email",userEmail);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        } else {
            return 0; // or return a default size if appropriate
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
