package com.samalapsy.githubjavadevelopers;

import com.androidnetworking.AndroidNetworking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.samalapsy.githubjavadevelopers.UserListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class DevelopersList extends AppCompatActivity {

    TextView username = null;
    ImageView imageView = null;
    RecyclerView recycler;
    UserListAdapter mAdapter;
    List<UserModel> userList = new ArrayList<>();
    UserModel userModel;
    RelativeLayout mainLayout;
    Snackbar snackbar;
    int page = 1;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers_list);

        imageView = (ImageView) findViewById(R.id.user_image);
        username = (TextView) findViewById(R.id.username);
        mainLayout = (RelativeLayout) findViewById(R.id.activity_developers_list);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(DevelopersList.this, 2));
        //recycler.setVisibility(View.VISIBLE);

        //Use SharedPreference to hold Current Page for the API
        prefs = this.getSharedPreferences(
                "com.samalapsy.githubjavadevelopers", Context.MODE_PRIVATE);
        editor = getSharedPreferences("com.samalapsy.githubjavadevelopers", MODE_PRIVATE).edit();
        editor.putInt("page", 1);
        editor.commit();


        //Check for internet Connection

        if (isInternetAvailable()) {
            snackbar = Snackbar
                    .make(mainLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        } else {
            //Load Data from Network
            loadData();

            mAdapter = new UserListAdapter(DevelopersList.this, userList, recycler);
            recycler.setAdapter(mAdapter);

            mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                   // Log.e("Where are you", "On Load More Now");
                    userList.add(null);
                    // Remove progress item
                    mAdapter.notifyItemRemoved(userList.size());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userList.remove(userList.size() - 1);
                            mAdapter.notifyItemRemoved(userList.size());

                            int start = userList.size();
                            final int end = start + 20;
                            final int cur = prefs.getInt("page", page++) + 1;
                            final int nextpage = cur;

                            //Log.e("Where are you", "OnLoad Network page=" + nextpage);


                            AndroidNetworking.get("https://api.github.com/search/users?q=location:lagos+language:java&type=Users&page=" + nextpage + "&per_page=40")
                                    .setTag("test")
                                    .setPriority(Priority.LOW)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                //Get the result from items Object
                                                JSONArray array = response.getJSONArray("items");

                                                //Loop through the User List
                                                for (int i = 0; i <= array.length(); i++) {
                                                    JSONObject obj = array.getJSONObject(i);

                                                    //add to Model
                                                    userModel = new UserModel(obj.getString("login"),
                                                            obj.getString("avatar_url"), obj.getString("html_url"));

                                                    userList.add(userModel);

                                                }

                                                // Save nextPage to Preference

                                                editor.putInt("page", cur);
                                                editor.commit();

                                                //Log.e("Current Page ", "Page " + nextpage + " Loaded Now");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            mAdapter.notifyDataSetChanged();
                                            mAdapter.setLoaded();
                                        }

                                        @Override
                                        public void onError(ANError anError) {

                                        }
                                    });
                        }
                    }, 7000);
                }
            });
        }
    }

    private void loadData() {
        final ProgressDialog pd = new ProgressDialog(DevelopersList.this);
        pd.isIndeterminate();
        pd.setMessage("Loading Developers...");
        pd.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Log.e("Where are you","LoadData Network");
                //AndroidNetworking.get("https://api.github.com/search/users?q=location:lagos+language:java&type=Users&page=1&per_page=40")
                AndroidNetworking.get("https://api.github.com/search/users?q=location:lagos+language:java&type=Users&page=1&per_page=40")
                        .setTag("test")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //Get the result from items Object
                                    JSONArray array = response.getJSONArray("items");

                                    //Loop through the User List
                                    for (int i = 0; i < array.length(); i++) {
                                        //for (int i = 0; i < 20; i++) {
                                        JSONObject obj = array.getJSONObject(i);

                                        //add to Model
                                        userModel = new UserModel(obj.getString("login"),
                                                obj.getString("avatar_url"), obj.getString("html_url"));
                                        userList.add(userModel);

                                    }




                                    if (!userList.isEmpty()) {
                                        pd.hide();
                                    }

                                    recycler.setHasFixedSize(true);
                                    mAdapter = new UserListAdapter(DevelopersList.this, userList, recycler);
                                    recycler.setAdapter(mAdapter);


                                    //pd.dismiss();
                                    //Log.e("New size","Added " + userList.size());
                                    /*recycler.setHasFixedSize(true);
                                    //recycler.setLayoutManager(new GridLayoutManager(DevelopersList.this, 2));
                                    //mAdapter = new UserListAdapter(DevelopersList.this ,userList, recycler);
                                    mAdapter = new UserListAdapter(DevelopersList.this ,userList, recycler);
                                    recycler.setAdapter(mAdapter);
                                    pd.hide();*/

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mAdapter.notifyDataSetChanged();
                                mAdapter.setLoaded();
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
            }
        });


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            Log.e("Internet Status", String.valueOf(ipAddr));
            return !ipAddr.equals("");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
