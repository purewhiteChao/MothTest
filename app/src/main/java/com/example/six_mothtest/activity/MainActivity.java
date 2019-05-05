package com.example.six_mothtest.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.six_mothtest.R;
import com.example.six_mothtest.adapter.MyRecyclerAdapter;
import com.example.six_mothtest.model.bean.GuShiBean;
import com.example.six_mothtest.model.bean.URLs;
import com.example.six_mothtest.model.db.DBNames;
import com.example.six_mothtest.model.db.MyDBDAO;
import com.example.six_mothtest.model.service.RetrofitService;
import com.example.six_mothtest.view.CurstomImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int PHOTO_CODE = 1;
    private final int LOAD_CODE = 100;
    private final int REFRESH_CODE = 200;
    private final int AVATER_CODE = 300;
    //TODO----------------------------------------------------------------
    private long lasttime = 0;
    private MyDBDAO myDBDAO;
    private int page = 1;
    private RecyclerView recycler_main;
    private MyRecyclerAdapter adapter;
    private SwipeRefreshLayout swiprefresh_main;
    private RetrofitService retrofitService;
    private CurstomImageView imageview_drawer;
    private String photoPath;


    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LOAD_CODE) {
                GuShiBean bean = (GuShiBean) msg.obj;
                List<GuShiBean.ResultBean> result = bean.getResult();
                adapter.refresh(result);
                for (int i = 0; i < result.size(); i++) {
                    ContentValues values = new ContentValues();
                    values.put(DBNames.COL_TITLE, result.get(i).getTitle());
                    values.put(DBNames.COL_AUTHOR, result.get(i).getAuthors());
                    values.put(DBNames.COL_CONTENT, result.get(i).getContent());
                    myDBDAO.inserDao(values);
                }
            } else if (msg.what == REFRESH_CODE) {
                GuShiBean bean = (GuShiBean) msg.obj;
                List<GuShiBean.ResultBean> result = bean.getResult();
                adapter.refresh(result);
                swiprefresh_main.setRefreshing(false);
                for (int i = 0; i < result.size(); i++) {
                    ContentValues values = new ContentValues();
                    values.put(DBNames.COL_TITLE, result.get(i).getTitle());
                    values.put(DBNames.COL_AUTHOR, result.get(i).getAuthors());
                    values.put(DBNames.COL_CONTENT, result.get(i).getContent());
                    myDBDAO.inserDao(values);
                }

            }else if(msg.what==AVATER_CODE){
                String json = (String) msg.obj;
                try {
                    JSONObject jsonBean = new JSONObject(json);
                    JSONObject data = jsonBean.getJSONObject("data");
                    String path = data.getString("url");
                    Log.i("GC","AvaterPath:"+path);
                    Glide.with(MainActivity.this).load(path).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageview_drawer.setImageDrawable(resource);
                            imageview_drawer.notifyImageView();
                            return true;
                        }
                    }).into(imageview_drawer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        initView();
        initData();
        initListener();
        //DisplayMetrics displayMetrics  = new DisplayMetrics();
        //https://github.com/purewhiteChao/MothTest.git
    }

    private void initListener() {
        swiprefresh_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page++;
                        if (page > 10) {
                            page = 1;
                        }
                        final Call<GuShiBean> guShi = retrofitService.getGuShi(URLs.GUSHI_URL, page + "", "10");
                        Executors.newCachedThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Response<GuShiBean> dataRefresh = guShi.execute();
                                    Message message = new Message();
                                    message.what = REFRESH_CODE;
                                    message.obj = dataRefresh.body();
                                    handler.sendMessage(message);
                                } catch (IOException e) {
                                    Log.i("GC", "无法正常获取");
                                    final List<GuShiBean.ResultBean> list = myDBDAO.selectDao();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.refresh(list);
                                            swiprefresh_main.setRefreshing(false);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }, 3000);
            }
        });
    }

    private void initData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.AVATER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
        final Call<GuShiBean> guShi = retrofitService.getGuShi(URLs.GUSHI_URL, "1", "10");
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<GuShiBean> execute = guShi.execute();
                    Message message = new Message();
                    message.what = LOAD_CODE;
                    message.obj = execute.body();
                    handler.sendMessage(message);

                } catch (IOException e) {
                    Log.i("GC", "无法正常获取");
                    List<GuShiBean.ResultBean> list = myDBDAO.selectDao();
                    adapter.refresh(list);
                }
            }
        });

    }

    private void initView() {
        recycler_main = (RecyclerView) findViewById(R.id.recycler_main);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_main.setLayoutManager(manager);
        adapter = new MyRecyclerAdapter();
        recycler_main.setAdapter(adapter);
        swiprefresh_main = (SwipeRefreshLayout) findViewById(R.id.swiprefresh_main);
        myDBDAO = new MyDBDAO();
        imageview_drawer = (CurstomImageView) findViewById(R.id.imageview_drawer);
        imageview_drawer.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lasttime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, "请再次点击退出按钮", Toast.LENGTH_SHORT).show();
        }
        lasttime = currentTimeMillis;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageview_drawer:
                startPhoto();
                break;
        }
    }

    private void startPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PHOTO_CODE&&data!=null){
            Uri photoUri = data.getData();
            Cursor query = getContentResolver().query(photoUri, null, null, null, null);
            while(query.moveToNext()){
                photoPath = query.getString(query.getColumnIndex("_data"));
                Log.i("GC","photoPath:"+photoPath);
            }
            query.close();
            uploadAvater(photoPath);

        }
    }

    public void uploadAvater(String path){
        File file = new File(path);
        RequestBody format = RequestBody.create(MediaType.parse("multipart/form-data"), "json");
        RequestBody smfileBody = RequestBody.create(MediaType.parse("application/otcet-stream"),file);
        MultipartBody.Part smfile = MultipartBody.Part.createFormData("smfile",file.getName(),smfileBody);
        final Call<ResponseBody> avater = retrofitService.getAvater(format, smfile);
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> execute = avater.execute();
                    Log.i("GC",execute.code()+"");
                    Message message = new Message();
                    message.obj = execute.body().string();
                    message.what = AVATER_CODE;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    Log.i("GC","请求头像失败");
                }
            }
        });
    }
}
