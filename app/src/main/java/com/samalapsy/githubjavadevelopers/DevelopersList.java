package com.samalapsy.githubjavadevelopers;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DevelopersList extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers_list);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AndroidNetworking.get("https://api.github.com/search/users?q=location:lagos+language:java&type=Users")
                        .setTag("test")
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    ImageView imageView = (ImageView) findViewById(R.id.user_image);
                                    TextView username = (TextView) findViewById(R.id.username);
                                    username.setText(response.getString("login"));
                                    Glide.with(DevelopersList.this).load(response.getString("avatar_url")).into(imageView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });


            }
        });
    }


    private class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

        private List<UserModel> mlist;

        public UserListAdapter(List<UserModel> list) {
            super();
            this.mlist = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return (mlist.size() < 0) ? 0 : mlist.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View view) {
                super(view);
            }
        }
    }
}
