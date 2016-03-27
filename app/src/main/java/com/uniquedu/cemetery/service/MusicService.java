package com.uniquedu.cemetery.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.uniquedu.cemetery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
public class MusicService extends Service {
    private MediaPlayer player;
    private List<Integer> music;
    private int musicid;
    private boolean isplay=false;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        musicid=intent.getIntExtra("musicid",0);
        Log.e("tag","当前播放歌曲的id"+musicid);
        if(player!=null){
            player.stop();
            player=MediaPlayer.create(this, music.get(musicid));
            player.setLooping(true);//开启循环模式
            player.start();
            isplay=true;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }
    @Override
    public void onCreate() {

        super.onCreate();
        build();

        player=MediaPlayer.create(this, music.get(0));
        player.setLooping(true);//开启循环模式
        player.start();

    }



    private void build() {
        music=new ArrayList<Integer>();
        music.add(R.raw.life_and_death);
        music.add(R.raw.father);
        music.add(R.raw.mother);
        music.add(R.raw.father_mother);


    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


}
