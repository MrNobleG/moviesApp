package com.example.moviesappproject.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.moviesappproject.Domain.Filmitem;
import com.example.moviesappproject.Domain.Rating;
import com.example.moviesappproject.R;
import com.google.gson.Gson;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private databaseHelper dbHelper;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressBar;
    private TextView titleTxt, movieIMDBRateTxt, movieRotRateTxt, Time, movieSummaryInfo, movieActorsInfo, RateTxt, MovieYear,Moviegenre;
    private String IDfilm,useremail;
    private ImageView backImg, pic;
    private NestedScrollView scrollView;
    private CheckBox favbox;
    String username;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper = new databaseHelper(this);
        useremail=getIntent().getStringExtra("email");
        Log.d("test1", "onCreate v3: "+useremail);
        user = dbHelper.getUserByEmail(useremail);
        Log.d("test 1", "onCreate: "+user.username);
        username =user.username;
        Log.d("test 1", "onCreate: "+username);
        IDfilm = getIntent().getStringExtra("id");


        initView();
        sendRequest();



        boolean isFilmInDatabase = isFilmInDatabase(IDfilm,username);
        favbox.setChecked(isFilmInDatabase);
    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        progressBar.setVisibility(View.VISIBLE);


        mStringRequest = new StringRequest(Request.Method.GET, "https://www.omdbapi.com/?apikey=b43975eb&i=" + IDfilm, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                Filmitem item = gson.fromJson(response, Filmitem.class);

                Glide.with(DetailActivity.this)
                        .load(item.getPoster())
                        .into(pic);

                titleTxt.setText(item.getTitle());
                movieIMDBRateTxt.setText(item.getImdbRating());
                movieActorsInfo.setText(item.getActors());
                Time.setText(item.getRuntime());
                MovieYear.setText(item.getYear());
                RateTxt.setText(item.getRated());
                movieSummaryInfo.setText(item.getPlot());
                String rottenTomatoesRating = getRottenTomatoesRating(item.getRatings());
                movieRotRateTxt.setText(rottenTomatoesRating);
                Moviegenre.setText(item.getGenre());


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Uilover2", "OneErrorResopnse:" + error.toString());

            }
        });

        mRequestQueue.add(mStringRequest);

    }

    private String getRottenTomatoesRating(List<Rating> ratings) {
        String rottenTomatoesRating = "N/A";
        for (Rating rating : ratings) {
            if (rating.getSource().equals("Rotten Tomatoes")) {
                rottenTomatoesRating = rating.getValue();
                break;
            }
        }
        return rottenTomatoesRating;
    }

        private void initView () {
            titleTxt = findViewById(R.id.moviename);
            progressBar = findViewById(R.id.progressBar4);
            scrollView = findViewById(R.id.scrollview2);
            movieIMDBRateTxt = findViewById(R.id.imdbreview);
            movieRotRateTxt = findViewById(R.id.rtoreview);
            Time = findViewById(R.id.movietime);
            MovieYear = findViewById(R.id.movieyear);
            RateTxt = findViewById(R.id.rate);
            movieSummaryInfo = findViewById(R.id.plot);
            movieActorsInfo = findViewById(R.id.Actor);
            backImg = findViewById(R.id.returnbtn);
            pic = findViewById(R.id.fullimage);
            Moviegenre = findViewById(R.id.genre);

            favbox=findViewById(R.id.checkBox);

            backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            favbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent open =new Intent(DetailActivity.this,DatabaseActivity.class);
                    open.putExtra("imdbId", IDfilm);
                    open.putExtra("username",username);
                    open.putExtra("isChecked", isChecked);
                    startActivity(open);
                }
            });

        }

    private boolean isFilmInDatabase(String imdbId, String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query("favorites", null, "imdbId = ? AND username = ?", new String[]{imdbId, username}, null, null, null);
        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    }
