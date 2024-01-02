package com.codeshare.photomotion.liewall;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.codeshare.photomotion.R;
import com.codeshare.photomotion.model.LiveWall;

import java.util.ArrayList;
import java.util.Collections;



    public class LiveWallAdapter extends RecyclerView.Adapter<LiveWallAdapter.MyViewHolder> {
        public LiveWallAdapter.ClickListener clickListener;
        private ArrayList<LiveWall> creationList;
        private Context mContext;

        public interface ClickListener {
            void onClick(int i);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ProgressBar progressBar;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                thumbnail =  view.findViewById(R.id.thumbnail);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            }
        }

        public LiveWallAdapter(Context context, LiveWallAdapter.ClickListener clickListener2) {
            mContext = context;
            clickListener = clickListener2;
        }

        public void setFiles(ArrayList<LiveWall> arrayList) {
//            Collections.reverse(arrayList);
            creationList = arrayList;
        }

        public LiveWall getPath(int i) {
            return  creationList.get(i);
        }

        public LiveWallAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new LiveWallAdapter.MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_album, viewGroup, false));
        }

        public void onBindViewHolder(final LiveWallAdapter.MyViewHolder myViewHolder, final int i) {

            myViewHolder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(creationList.get(i).getImage_url()).listener(new RequestListener<Drawable>() {
                public boolean onLoadFailed(@Nullable GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                    myViewHolder.progressBar.setVisibility(View.GONE);
                    return false;

                }
            }).into(myViewHolder.thumbnail);
            myViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    clickListener.onClick(i);
                }
            });
        }

        public int getItemCount() {
            return creationList.size();
        }
    }

