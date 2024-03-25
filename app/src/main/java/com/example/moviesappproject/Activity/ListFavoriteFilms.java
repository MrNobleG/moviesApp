package com.example.moviesappproject.Activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesappproject.Adapter.FilmFavoriteAdapter;
import com.example.moviesappproject.Domain.Filmitem;
import com.example.moviesappproject.Domain.OMDBResponse;
import com.example.moviesappproject.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ListFavoriteFilms extends AppCompatActivity {
    private static final String TAG = "DatabaseAccess";
    private SQLiteDatabase db;
    RecyclerView.Adapter adapterBestMovies;
    private RecyclerView recyclerViewBestMovies;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar loading;

    private TextView message;
    List<OMDBResponse> filmDataList = new ArrayList<>();
    protected  String rech;
    protected String useremail,username;
    private User user;
    Cursor cursor;
    List<Filmitem> filmItemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_list_favorite_films);
        databaseHelper favDB = new databaseHelper(this);
        db = favDB.getWritableDatabase();

        super.onCreate(savedInstanceState);

        useremail=getIntent().getStringExtra("email");
        user=favDB.getUserByEmail(useremail);
        username =user.username;


        Log.d("test", "onCreate: we are here");

        initView();
        sendRequest();
    }


    protected void sendRequest() {
        cursor = db.rawQuery("SELECT imdbId FROM favorites WHERE username = ?", new String[]{username});

        Log.d("test", "onCreate: we are here on fav");
        Log.d("test", "Cursor status: " + (cursor != null ? "Not null" : "Null"));

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String filmId = cursor.getString(cursor.getColumnIndex("imdbId"));
                Log.d("test1", "Favorite inserted successfully: " + filmId+cursor.getCount());
                fetchFilmData(filmId);
            } while (cursor.moveToNext());

        }
    }

    private void fetchFilmData(String filmId) {
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, "https://www.omdbapi.com/?apikey=b43975eb&i=" +filmId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("test", "Raw Response: " + response);

                Gson gson = new Gson();

                Filmitem filmItem = gson.fromJson(response, Filmitem.class);
                if (filmItem!=null){
                    Log.d("test", "Received film data: " + filmItem.getTitle());
                }
                // Instead of adding to filmDataList, create a list of Search objects

                filmItemList.add(filmItem);
                // Create and set the adapter with the list of Search objects
                if (filmItemList!=null){
                Log.d("test", "its null "+filmItemList.size());
                }
                if (filmItemList.size() == cursor.getCount()) {
                    loading.setVisibility(View.GONE); // Hide loading progress bar
                    message.setVisibility(View.GONE);
                    adapterBestMovies = new FilmFavoriteAdapter(filmItemList,useremail);
                    recyclerViewBestMovies.setAdapter(adapterBestMovies);
                }
                if (filmItemList.isEmpty()){
                    recyclerViewBestMovies.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    message.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Log.d("Uilover3", "OneErrorResopnse:" + error.toString());
                Log.e("test", "Volley error occurred while fetching film data", error);
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void initView() {

        message=findViewById(R.id.nofavorites);
        recyclerViewBestMovies = findViewById(R.id.Recyclerfavorites);
        recyclerViewBestMovies.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loading = findViewById(R.id.progressBar6);
    }
}


