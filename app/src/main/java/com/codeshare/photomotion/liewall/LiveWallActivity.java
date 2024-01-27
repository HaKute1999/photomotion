package com.codeshare.photomotion.liewall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog.Builder;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.expert.photo2video.R;
import com.codeshare.photomotion.activity.BaseActivity;
import com.codeshare.photomotion.activity.MainActivity;
import com.codeshare.photomotion.model.LiveWall;
import com.codeshare.photomotion.photoAlbum.AlbumAdapter.ClickListener;
import com.codeshare.photomotion.photoAlbum.AlbumHelper;
import com.codeshare.photomotion.photoAlbum.VideoPreviewActivity;
import com.codeshare.photomotion.utils.AppHelper;
import com.codeshare.photomotion.utils.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LiveWallActivity extends BaseActivity implements OnClickListener {
    public LiveWallAdapter adapter;
    public List<LiveWall> allFiles = new ArrayList<>();
    public ArrayList<String> favFiles = new ArrayList<>();
    public ImageView ivAllDelete;
    public ImageView ivNoPhoto;
    public ProgressBar progressBar;
    public RecyclerView rvThumbs;
    public int selectedTab = 0;
    public int sizeOfAllFiles = -1;
    public int sizeOfFavFiles = -1;
    private ImageView ivBackMyPhotos;
    private long mLastClickTime = 0;
    private TextView tvAll;
    public Context getContext() {
        return this;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_live_wall);
//        Intent intent = getIntent();
//        String response = intent.getStringExtra("api");
//
//         allFiles = new ArrayList<>();
//        if (response!= null){
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<LiveWall>>() {}.getType();
//
//            allFiles = gson.fromJson(response, listType);
//        }
    }

    public void initViews() {
        ivBackMyPhotos =  findViewById(R.id.iv_back_my_photos);
        ivAllDelete =  findViewById(R.id.iv_all_delete);
        tvAll =  findViewById(R.id.tvAll);
//        tvFav =  findViewById(R.id.tvFav);
        rvThumbs =  findViewById(R.id.rv_thumbs);
        ivNoPhoto =  findViewById(R.id.lin_no_photo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rvThumbs.setLayoutManager(new GridLayoutManager(this, 3));
        rvThumbs.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(5), true));
        rvThumbs.setItemAnimator(new DefaultItemAnimator());
        tvAll.setSelected(true);
//        tvFav.setSelected(false);
//        tvAll.setTextColor(-1);
//        tvFav.setTextColor(getResources().getColor(R.color.colorPrimary));
//        FrameLayout mAdView = findViewById(R.id.adView);
//        loadBannerAdsApp(mAdView);
    }

    public void initData() {

        adapter = new LiveWallAdapter(mContext, new LiveWallAdapter.ClickListener() {
            @Override
            public void onClick(int i) {
                LiveWall path = adapter.getPath(i);
                String str = "isfav";
                Intent intent2 = new Intent(mContext, VideoPreviewActivity.class);
                intent2.putExtra("video_path", path.getVideo_url());
                intent2.putExtra(str, 0);
                startActivityForResult(intent2, 101);
            }
        });
        new LoadFileTask().execute(new Void[0]);
        adapter.setFiles((ArrayList<LiveWall>) allFiles);
        rvThumbs.setAdapter(adapter);
    }

    public void setSelection() {
        if (selectedTab == 0) {
            tvAll.setSelected(true);
//            tvFav.setSelected(false);
//            tvAll.setTextColor(-1);
//            tvFav.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (allFiles.isEmpty()) {
                ivNoPhoto.setVisibility(View.VISIBLE);
                rvThumbs.setVisibility(View.GONE);
                ivAllDelete.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPlanoDeFundoTransparente), Mode.SRC_IN);
                return;
            }
            adapter.setFiles((ArrayList<LiveWall>) allFiles);
            adapter.notifyDataSetChanged();
            ivNoPhoto.setVisibility(View.GONE);
            rvThumbs.setVisibility(View.VISIBLE);
            ivAllDelete.setColorFilter(ContextCompat.getColor(mContext, R.color.white), Mode.SRC_IN);
        } else if (selectedTab == 1) {
//            tvAll.setSelected(false);
////            tvFav.setSelected(true);
////            tvAll.setTextColor(getResources().getColor(R.color.colorPrimary));
////            tvFav.setTextColor(-1);
//            if (favFiles.isEmpty()) {
//                rvThumbs.setVisibility(View.GONE);
//                ivNoPhoto.setVisibility(View.VISIBLE);
//                ivAllDelete.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPlanoDeFundoTransparente), Mode.SRC_IN);
//                return;
//            }
//            rvThumbs.setVisibility(View.VISIBLE);
//            adapter.setFiles(favFiles);
//            adapter.notifyDataSetChanged();
//            ivNoPhoto.setVisibility(View.GONE);
//            ivAllDelete.setColorFilter(ContextCompat.getColor(mContext, R.color.white), Mode.SRC_IN);
        }
    }

    public void initActions() {
        ivBackMyPhotos.setOnClickListener(this);
        tvAll.setOnClickListener(this);
//        tvFav.setOnClickListener(this);
        // ivAllDelete.setOnClickListener(this);
    }

    public void ConfirmationDialog() {
        Builder builder = new Builder(mContext);
        builder.setMessage((CharSequence) "Are you sure want to delete all gif & videos?");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                new DeleteFileTask().execute(new Void[0]);
            }
        });
        builder.setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private int dpToPx(int i) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, getResources().getDisplayMetrics()));
    }

    public void onPause() {

        super.onPause();
    }

    public void onResume() {
        new LoadFileTask().execute(new Void[0]);


        super.onResume();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 101) {
            new LoadFileTask().execute(new Void[0]);
        }
    }

    public void onClick(View view) {
        int i;
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime();
            String str = " : ";
            String str2 = "onClick: ";
            switch (view.getId()) {
                case R.id.iv_all_delete:
                    if (selectedTab == 0) {
                        i = sizeOfAllFiles;
                    } else {
                        i = sizeOfFavFiles;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(sizeOfAllFiles);
                    sb.append(str);
                    sb.append(sizeOfFavFiles);
                    Log.e("SIZE", sb.toString());
                    if (i > 0) {
                        ConfirmationDialog();
                        break;
                    }
                    break;
                case R.id.iv_back_my_photos:
                    onBackPressed();
                    break;
                case R.id.tvAll:
                    selectedTab = 0;
                    setSelection();
                    break;
            }
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(mContext, MainActivity.class));
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        finish();
    }

    public class DeleteFileTask extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            if (selectedTab == 0) {
                for (int i = 0; i < allFiles.size(); i++) {
//                    AlbumHelper.delete((String) allFiles.get(i));
                }
                allFiles.clear();
                allFiles = new ArrayList();
                sizeOfAllFiles = -1;
            } else {
//                favFiles.clear();
//                favFiles = new ArrayList();
//                favHelper.deleteAllFav();
//                sizeOfFavFiles = -1;
            }
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            progressBar.setVisibility(View.GONE);
            rvThumbs.setVisibility(View.GONE);
            ivNoPhoto.setVisibility(View.VISIBLE);
            ivAllDelete.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPlanoDeFundoTransparente), Mode.SRC_IN);
            StringBuilder sb = new StringBuilder();
            sb.append("onPostExecute: All:");
            sb.append(allFiles.size());
            String str = "AAAAA";
            Log.i(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onPostExecute: Fav:");
            sb2.append(allFiles.size());
            Log.i(str, sb2.toString());
        }
    }


    public class LoadFileTask extends AsyncTask<Void, Void, Void> {
        public void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        public Void doInBackground(Void... voidArr) {
            LiveWallActivity LiveWallActivity = LiveWallActivity.this;
            Intent intent = getIntent();
            String response = intent.getStringExtra("api");
            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<LiveWall>>() {}.getType();
            if (response!= null){



                try {
                    LiveWallActivity.allFiles = getResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Log.e("Download ", AppHelper.verifyLoginAndGetData());

                            LiveWallActivity.allFiles =getResponse(AppHelper.verifyLoginAndGetData());

                        } catch (IOException e) {
                            Log.e("Exception ", "" + e.getMessage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//

                    }
                }).start();
            }
//            LiveWallActivity.allFiles = AlbumHelper.loadFiles(LiveWallActivity.mContext);
            LiveWallActivity LiveWallActivity2 = LiveWallActivity.this;
//            LiveWallActivity2.favFiles = LiveWallActivity2.favHelper.getAllFav();
//            Collections.reverse(favFiles);
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            progressBar.setVisibility(View.GONE);
            if (!(sizeOfAllFiles == allFiles.size() && sizeOfFavFiles == favFiles.size())) {
                LiveWallActivity LiveWallActivity = LiveWallActivity.this;
                LiveWallActivity.sizeOfAllFiles = LiveWallActivity.allFiles.size();
                LiveWallActivity LiveWallActivity2 = LiveWallActivity.this;
                LiveWallActivity2.sizeOfFavFiles = LiveWallActivity2.favFiles.size();
                setSelection();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("onPostExecute: All:");
            sb.append(allFiles.size());
            String str = "AAAAA";
            Log.i(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onPostExecute: Fav:");
            sb2.append(allFiles.size());
            Log.i(str, sb2.toString());
        }
    }
    private ArrayList<LiveWall> getResponse(String response) throws JSONException {
        ArrayList<LiveWall> liveWalls = new ArrayList<>();
        Gson goson = new Gson();
        JSONObject jsonObject = new JSONObject(response);
//        Type type = new TypeToken<List<LiveWall>>() {}.getType();
//        JSONArray array = jsonObject.getJSONArray("videos");

//        List<LiveWall> lista = goson.fromJson(String.valueOf(array), type);

        JSONArray arr = jsonObject.getJSONArray("videos");
        if (arr != null) {
            for (int i=0;i<arr.length();i++){
//                String t = arr.get(i).toString();
//                LiveWall live = new Gson().fromJson(t, LiveWall.class);
                liveWalls.add(new LiveWall(arr.getJSONObject(i).getString("video_url"), arr.getJSONObject(i).getString("image_url")));
            }
        }
//        Type listType = new TypeToken<ArrayList<LiveWall>>(){}.getType();


//        TypeToken<List<LiveWall>> token = new TypeToken<List<LiveWall>>(){};

        return liveWalls;

    }
}
