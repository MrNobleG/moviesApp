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
import com.example.moviesappproject.Activity.DetailActivity1;
import com.example.moviesappproject.Domain.ListFilm1;
import com.example.moviesappproject.R;

public class FilmListAdapter1 extends RecyclerView.Adapter<FilmListAdapter1.ViewHolder> {
    ListFilm1 items;

    Context context;

    @NonNull
    @Override
    public FilmListAdapter1.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate =LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_film1,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmListAdapter1.ViewHolder holder, int position) {
        holder.titleText.setText(items.getData().get(position).getTitle());
        holder.ScoreText.setText(""+items.getData().get(position).getImdbRating());
        Glide.with(holder.itemView.getContext()).load(items.getData().get(position).getPoster()).into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity1.class);
                intent.putExtra("id",items.getData().get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }


    public FilmListAdapter1(ListFilm1 items) {
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
