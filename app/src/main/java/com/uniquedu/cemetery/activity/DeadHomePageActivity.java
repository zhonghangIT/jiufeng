package com.uniquedu.cemetery.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.adapter.DeadHomePagerAdapter;
import com.uniquedu.cemetery.bean.Dead;
import com.uniquedu.cemetery.bean.DeadCallBack;
import com.uniquedu.cemetery.fragment.AnthologyFragment;
import com.uniquedu.cemetery.fragment.DeadInfomationFragment;
import com.uniquedu.cemetery.fragment.DeadWorshipFragment;
import com.uniquedu.cemetery.fragment.PhotoFragment;
import com.uniquedu.cemetery.fragment.WorshipDailyFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongHang on 2016/3/4.
 * 表示逝者的界面
 */
public class DeadHomePageActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<BaseFragment> mPages;
    private DeadHomePagerAdapter mAdapter;
    public Dead mDead;
    private DeadInfomationFragment deadInfoFragment;
    private DeadWorshipFragment deadFragment;
    private WorshipDailyFragment worshipDailyFragment;
    private PhotoFragment photoFragment;
    public  DeadCallBack callBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();
    }
    public void toInfomation(){
        mViewPager.setCurrentItem(1,false);
    }
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentManager manager = getSupportFragmentManager();
        mPages = new ArrayList<>();
        deadFragment=new DeadWorshipFragment();
        deadFragment.setTitle("在线祭扫");
        mPages.add(deadFragment);
        deadInfoFragment = new DeadInfomationFragment();
        deadInfoFragment.setTitle("逝者资料");
        mPages.add(deadInfoFragment);
        worshipDailyFragment=new WorshipDailyFragment();
        worshipDailyFragment.setTitle("祭扫日志");
        mPages.add(worshipDailyFragment);
        photoFragment=new PhotoFragment();
        photoFragment.setTitle("历史相册");
        mPages.add(photoFragment);
        AnthologyFragment anthologyFragment=new AnthologyFragment();
        anthologyFragment.setTitle("纪念文选");
        mPages.add(anthologyFragment);
        mAdapter = new DeadHomePagerAdapter(manager, mPages);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfomation();
    }

    /**
     * 请求到灵堂信息，以及逝者的详细信息，刷新灵堂界面以及详细信息界面
     */
    private void getInfomation() {
        mDead = (Dead) getIntent().getExtras().getSerializable("dead");
        StringRequest request = new StringRequest(Request.Method.GET, Address.INFOMATION + mDead.getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);

                if (TextUtils.isEmpty(response)) {
                    return;
                }
                Log.d("fengbeizaixian",response);
                response = response.trim();
                if (response.indexOf("callbakename") == 0) {
                    response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                }
                Gson gson = new Gson();
                Type typeToken = new TypeToken<DeadCallBack>() {
                }.getType();
                callBack = gson.fromJson(response, typeToken);
                deadInfoFragment.infodate(callBack.getRows());
                deadFragment.initData(callBack);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "获取详细信息失败", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
