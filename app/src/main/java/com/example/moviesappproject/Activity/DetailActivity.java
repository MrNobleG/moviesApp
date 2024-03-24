package com.example.moviesappproject.Activity;

import static androidx.recyclerview.widget.RecyclerView.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.moviesappproject.Adapter.imageListAdapter;
import com.example.moviesappproject.Domain.FilmItem;
import com.example.moviesappproject.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private TextView title,movieRate,movieTime,movieDate,movieSummary,movieActorsInfo;
    private NestedScrollView scrollView;
    private int idFilm;
    private ShapeableImageView pic1;
    private ImageView pic2,backImg;
    private Adapter adapterImgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        idFilm=getIntent().getIntExtra("id",0);
        initView();
        sendRequest();
    }
    public void sendRequest(){
        mRequestQueue = Volley.newRequestQueue(this);

        scrollView.setVisibility(View.VISIBLE);

        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies/"+idFilm, response -> {
            Gson gson = new Gson();



            FilmItem item = gson.fromJson(response,FilmItem.class);
            Glide.with(DetailActivity.this).load(item.getPoster()).into(pic1);
            Glide.with(DetailActivity.this).load(item.getPoster()).into(pic2);
            title.setText(item.getTitle());
            movieRate.setText(item.getImdbRating());
            movieTime.setText(item.getRuntime());
            movieDate.setText(item.getReleased());

            if(item.getImages()!=null){
                adapterImgList = new imageListAdapter(item.getImages());

            }

        }, error -> {

            Log.i("youssef","onErrorResponse"+error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }
    private void initView(){
        title = findViewById(R.id.movieTitle);
        scrollView = findViewById(R.id.scroll);
        pic1=findViewById(R.id.posterNormalMg);
        pic2=findViewById(R.id.psterBigImg);
        movieRate = findViewById(R.id.movieRating);
        movieTime = findViewById(R.id.movieTime);
        movieDate = findViewById(R.id.movieCal);

        backImg = findViewById(R.id.backImg);
        backImg.setOnClickListener(v ->finish());


    }
}