package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.service.MusicService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/9.
 */
public class MPlayerActivity extends BaseActivity {
    private String[] musicNames = {"生死不离","父亲","母亲","父亲母亲"};
    private String[] musicTime = {"刘和刚  04:11","彭丽媛  04:33","彭丽媛  04:40",
            "王宏伟  04:41"};
    private int [] musicrul = {R.raw.life_and_death,R.raw.father,R.raw.mother,R.raw.father_mother};
    private ListView musicList;
    private List<Map<String, String>> listems;
    private ImageView music_image;
    private TextView play_back;
    private RequestQueue mQueue;
    private int id;//纪念馆id

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        initialize();
        getmusicdata();
        lvclick();


    }

    private void initialize() {

        play_back = (TextView) findViewById(R.id.play_back);
        musicList = (ListView) findViewById(R.id.music_list);
        play_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }

    private void getmusicdata() {

        listems = new ArrayList<Map<String, String>>();
        for (int i = 0; i < musicNames.length; i++) {
            Map<String, String> listem = new HashMap<String,String>();
            listem.put("name", musicNames[i]);
            listem.put("time", musicTime[i]);
            listems.add(listem);
        }
        SimpleAdapter simplead = new SimpleAdapter(this, listems,
                R.layout.list_music, new String[] { "name", "time" },
                new int[] {R.id.text_music_name,R.id.text_music_time});
        musicList.setAdapter(simplead);

    }

    private void lvclick() {
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(MPlayerActivity.this, MusicService.class);
                it.putExtra("musicid", position);
                startService(it);
                sendMusicId(position+1);
            }
        });
    }


    private void sendMusicId(final int i) {
        StringRequest srReq = new StringRequest(Request.Method.GET, "http://123.56.236.240/mvcwebmis/nologin/ChangeMp3State?callback=callbakename&id=3079&state=" + i,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("tag","发送成功"+i);
                    }
                },null);
        mQueue.add(srReq);
    }
}
