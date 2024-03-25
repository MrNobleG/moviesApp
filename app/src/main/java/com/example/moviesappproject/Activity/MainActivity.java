package com.example.moviesappproject.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesappproject.Adapter.FilmListAdapter1;
import com.example.moviesappproject.Domain.ListFilm1;
import com.example.moviesappproject.R;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterNewMovies,adapterUpcoming;
    private RecyclerView recyclerViewNewMovies , recyclerViewUpcomingMovies;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest,mStringRequest2;
    private ProgressBar loading1,loading2;

    private TextView searchbt;
    ImageView favorites;


    String useremail;
    String sendemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        innitView();
       sendRequest1();
       sendRequest2();


        useremail = getIntent().getStringExtra("useremail");
        sendemail = useremail;
        search();
        ImageView profile = findViewById(R.id.profile);
        favorites =findViewById(R.id.favorite);

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ListFavoriteFilms.class);
                intent.putExtra("email",sendemail);
                startActivity(intent);
                overridePendingTransition(R.anim.transition, R.anim.transition2);
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProfileViewActivity.class);
                intent.putExtra("email",sendemail);
                startActivity(intent);
                overridePendingTransition(R.anim.transition, R.anim.transition2);
            }
        });

    }

    private void search() {
        searchbt=findViewById(R.id.editText2);
        searchbt.getText().toString();
        searchbt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    Intent in =new Intent(MainActivity.this,ListFilmsActivity.class);
                    in.putExtra("email",sendemail);
                    in.putExtra("search",searchbt.getText().toString());
                    startActivity(in);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"You Logged out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        overridePendingTransition(R.anim.transition, R.anim.transition2);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private void sendRequest1(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm1 items=gson.fromJson(response, ListFilm1.class);
            adapterNewMovies = new FilmListAdapter1(items);
            recyclerViewNewMovies.setAdapter(adapterNewMovies);

        }, error -> {
            Log.i("youssef","sendRequest1 : "+error.toString());
            loading1.setVisibility(View.GONE);
        });
        mRequestQueue.add(mStringRequest);
    }
    private void sendRequest2(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        mStringRequest2 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=3", response -> {
            Gson gson = new Gson();
            loading2.setVisibility(View.GONE);
            ListFilm1 items=gson.fromJson(response, ListFilm1.class);
            adapterUpcoming = new FilmListAdapter1(items);
            recyclerViewUpcomingMovies.setAdapter(adapterUpcoming);

        }, error -> {
            Log.i("youssef","sendRequest2 : "+error.toString());
            loading2.setVisibility(View.GONE);
        });
        mRequestQueue.add(mStringRequest2);
    }
    private void innitView(){
        recyclerViewNewMovies=findViewById(R.id.view1);
        recyclerViewNewMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewUpcomingMovies = findViewById(R.id.view2);
        recyclerViewUpcomingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        loading1=findViewById(R.id.loading1);
        loading2=findViewById(R.id.loading2);
    }
}