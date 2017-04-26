package com.samalapsy.githubjavadevelopers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProfileView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        ImageView imageView = (ImageView) findViewById(R.id.user_image);
        TextView usernameView = (TextView) findViewById(R.id.username);
        TextView user_profile_linkView = (TextView) findViewById(R.id.user_profile_link);

        // Get Bundles Extras
        Bundle b = getIntent().getExtras();

        String usernameText = b.getString("username");
        usernameView.setText("@" + usernameText);

        String url = b.getString("url");
        user_profile_linkView.setText(url);

        //Load Image Url into Glide from Bundle
        String avatar = b.getString("avatar");
        Glide.with(this).load(avatar).placeholder(R.drawable.avatar).into(imageView);

        // Parse Uri to string
        final String profile_url = String.valueOf(Uri.parse(b.getString("url")));


        // Set Onclick for the Github Profile link and Open in Browser
        user_profile_linkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(profile_url));
                startActivity(browserIntent);
            }
        });


    }
}
