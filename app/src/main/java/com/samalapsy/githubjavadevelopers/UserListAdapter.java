package com.samalapsy.githubjavadevelopers;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.samalapsy.githubjavadevelopers.R.id.username;

//public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
public class UserListAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private Context mContext;
    private List<UserModel> mlist = new ArrayList<>();
    private int visibleThreshold = 20;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    UserListAdapter(Context c, List<UserModel> list, RecyclerView recyclerView) {
        //UserListAdapter(Context c, List<UserModel> list) {
        this.mContext = c;
        this.mlist = list;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            //final LinearLayoutManager layoutManager = (LinearLayoutManager) view.getLayoutManager();
            final GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });

        }
    }

    @Override
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        Log.e("View Type", String.valueOf(viewType));
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gridlist_item, parent, false);
            Log.e("View Type", "GridList View");
            vh = new UserViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
            Log.e("View Type", "ProgressBar");
            vh = new ProgressViewHolder(itemView);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //if (holder != null) {
        if (holder instanceof UserViewHolder) {
            //UserModel user = (UserModel) mlist.get(position);
            final UserModel user = mlist.get(position);
            ((UserViewHolder) holder).username.setText(user.getUsername());

            Glide.with(mContext)
                    .load(user.getImage())
                    .placeholder(R.drawable.avatar)
                    .into(((UserViewHolder) holder).user_image);

            ((UserViewHolder) holder).itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(mContext, ProfileView.class);
                    Bundle b = new Bundle();
                    b.putString("username", user.getUsername());
                    b.putString("avatar", user.getImage());
                    b.putString("url", user.getUrl());
                    profileIntent.putExtras(b);
                    mContext.startActivity(profileIntent);
                    //Toast.makeText(v.getContext(), user.getUsername(), Toast.LENGTH_SHORT).show();

                }
            });


            ((UserViewHolder) holder).btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Share Intent here
                    //Check out this awesome developer @<github username>, <github profile url>
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer @" +
                            user.getUsername() + ", " + user.getUrl());
                    sendIntent.setType("text/plain");
                    mContext.startActivity(sendIntent);
                }
            });


        } else ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

    }

    /*@Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //UserViewHolder vh = null;
        RecyclerView.ViewHolder vh = null;
        *//*View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridlist_item, parent, false);
                //.inflate(R.layout.gridlist_item, null);
        //Log.e("View Type", "GridList View");
        return new UserViewHolder(itemView);*//*

       if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gridlist_item, parent, false);
            Log.e("View Type", "GridList View");
            vh = new UserViewHolder(itemView);
        }else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_bar, parent, false);
            Log.e("View Type", "ProgressBar");
            vh = new ProgressViewHolder(itemView);
        }

        return  vh;

    }*/

   /* @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (holder != null) {
            //UserModel user = (UserModel) mlist.get(position);
            final UserModel user = mlist.get(position);
            holder.username.setText(user.getUsername());

            Glide.with(mContext)
                    .load(user.getImage())
                    .placeholder(R.drawable.avatar)
                    .into(holder.user_image);

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(mContext, ProfileView.class);
                    Bundle b = new Bundle();
                    b.putString("username", user.getUsername());
                    b.putString("avatar", user.getImage());
                    b.putString("url", user.getAvatar());
                    profileIntent.putExtras(b);
                    mContext.startActivity(profileIntent);
                    //Toast.makeText(v.getContext(), user.getUsername(), Toast.LENGTH_SHORT).show();

                }
            });


            holder.btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Share Intent here
                    //Check out this awesome developer @<github username>, <github profile url>
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome developer @" +
                            user.getUsername() + ", " + user.getAvatar());
                    sendIntent.setType("text/plain");
                    mContext.startActivity(sendIntent);
                }
            });

        }

    }*/

    @Override
    public int getItemViewType(int position) {
        int VIEW_PROG = 0;
        return mlist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image, btnShare;
        RelativeLayout itemLayout;
        TextView username;
        Context mContext;
        UserModel user;

        public UserViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            user_image = (ImageView) view.findViewById(R.id.user_image);
            btnShare = (ImageView) view.findViewById(R.id.share_button);
            username = (TextView) view.findViewById(R.id.username);
            itemLayout = (RelativeLayout) view.findViewById(R.id.main_layout);

        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }

    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
}

