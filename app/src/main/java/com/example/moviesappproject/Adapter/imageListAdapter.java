package com.example.moviesappproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesappproject.R;

import java.util.List;

public class imageListAdapter extends RecyclerView.Adapter<imageListAdapter.ViewHolder> {
    List<String> images;
    Context context;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_detail,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }




    @Override
    public void onBindViewHolder(@NonNull imageListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(images.get(position)).into(holder.pic);
    }

    public imageListAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.itemImages);
        }
    }
}
