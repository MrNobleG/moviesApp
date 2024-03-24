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
import com.example.moviesappproject.Activity.DetailActivity;
import com.example.moviesappproject.Domain.ListFilm;
import com.example.moviesappproject.Domain.FilmItem;
import com.example.moviesappproject.R;

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.ViewHolder> {
    ListFilm items;

    Context context;

    @NonNull
    @Override
    public FilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate =LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_film,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListAdapter.ViewHolder holder, int position) {
        holder.titleText.setText(items.getData().get(position).getTitle());
        holder.ScoreText.setText(""+items.getData().get(position).getImdbRating());
        Glide.with(holder.itemView.getContext()).load(items.getData().get(position).getPoster()).into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("id",items.getData().get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }


    public FilmListAdapter(ListFilm items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText,ScoreText;
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title);
            ScoreText = itemView.findViewById(R.id.Score);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
