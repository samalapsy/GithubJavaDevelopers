package com.samalapsy.githubjavadevelopers;

import com.androidnetworking.AndroidNetworking;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
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

            /*if (userList.isEmpty()) {
                recycler.setVisibility(View.GONE);
                Log.e("Where are you","List is Empty");
            } else {
                Log.e("Where are you","List Is Not Empty");

                mAdapter.setOnLoadMoreListener(new UserListAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        Log.e("Where are you","On Load More Now");
                        userList.add(null);
                        // Remove progress item
                        mAdapter.notifyItemRemoved(userList.size());


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                userList.remove(userList.size() - 1);
                                mAdapter.notifyItemRemoved(userList.size());

                                int start = userList.size();
                                final int end = start + 14;

                                Log.e("Where are you","OnLoad Network");

                                AndroidNetworking.get("https://api.github.com/search/users?q=location:lagos+language:java&type=Users")
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
                                                    for (int i = 0; i <= end; i++) {
                                                        JSONObject obj = array.getJSONObject(i);

                                                        //add to Model
                                                        userModel = new UserModel(obj.getString("login"),
                                                                obj.getString("avatar_url"), obj.getString("html_url"));

                                                        userList.add(userModel);

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                mAdapter.notifyDataSetChanged();
                                                //mAdapter.notifyItemInserted(userList.size());
                                                mAdapter.setLoaded();
                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                            }
                        });

                    }
                });
            }*/
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
                AndroidNetworking.get("https://api.github.com/search/users?q=location:lagos+language:java&type=Users")
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
                                    //for (int i = 0; i < array.length(); i++) {
                                    for (int i = 0; i < 20; i++) {
                                        JSONObject obj = array.getJSONObject(i);

                                        //add to Model
                                        userModel = new UserModel(obj.getString("login"),
                                                obj.getString("avatar_url"), obj.getString("html_url"));
                                        userList.add(userModel);

                                    }

                                    //pd.dismiss();
                                    //Log.e("New size","Added " + userList.size());

                                    recycler.setHasFixedSize(true);
                                    //recycler.setLayoutManager(new GridLayoutManager(DevelopersList.this, 2));
                                    //mAdapter = new UserListAdapter(DevelopersList.this ,userList, recycler);
                                    mAdapter = new UserListAdapter(DevelopersList.this ,userList);
                                    recycler.setAdapter(mAdapter);
                                    pd.hide();

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
