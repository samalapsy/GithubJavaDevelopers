package com.samalapsy.githubjavadevelopers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ProfileView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        ImageView imageView = (ImageView) findViewById(R.id.user_image);

        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
    }
}
