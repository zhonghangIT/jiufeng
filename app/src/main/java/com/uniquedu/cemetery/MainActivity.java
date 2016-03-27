package com.uniquedu.cemetery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uniquedu.cemetery.adapter.MainPagerAdapter;
import com.uniquedu.cemetery.fragment.CenterFragment;
import com.uniquedu.cemetery.fragment.DeadGridFragment;
import com.uniquedu.cemetery.fragment.InfomationFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TextView mTextViewInfomation;
    private TextView mTextViewWorship;
    private TextView mTextViewCenter;
    private TextView mTextViewScan;
    public static final int INFOMATION = 0;
    public static final int WORSHIP = 1;
    public static final int CENTER = 2;
    private List<Fragment> mPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTextViewInfomation = (TextView) findViewById(R.id.textview_infomation);
        mTextViewWorship = (TextView) findViewById(R.id.textview_worship);
        mTextViewCenter = (TextView) findViewById(R.id.textview_center);
        mTextViewScan = (TextView) findViewById(R.id.textview_scan);
        mTextViewInfomation.setOnClickListener(this);
        mTextViewWorship.setOnClickListener(this);
        mTextViewCenter.setOnClickListener(this);
        mTextViewScan.setOnClickListener(this);
        mPages=new ArrayList<>();
        mPages.add(new InfomationFragment());
        mPages.add(new DeadGridFragment());
        mPages.add(new CenterFragment());
        mViewPager.setOffscreenPageLimit(3);
        FragmentManager manager=getSupportFragmentManager();
        MainPagerAdapter pagerAdapter=new MainPagerAdapter(manager,mPages);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTextViewInfomation.setSelected(true);
    }

    public void selectTab() {
        mTextViewInfomation.setSelected(false);
        mTextViewWorship.setSelected(false);
        mTextViewCenter.setSelected(false);
        mTextViewScan.setSelected(false);
        switch (mViewPager.getCurrentItem()) {
            case INFOMATION:
                mTextViewInfomation.setSelected(true);
                break;
            case WORSHIP:
                mTextViewWorship.setSelected(true);
                break;
            case CENTER:
                mTextViewCenter.setSelected(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_infomation:
                mViewPager.setCurrentItem(INFOMATION,false);
                break;
            case R.id.textview_worship:
                mViewPager.setCurrentItem(WORSHIP,false);
                break;
            case R.id.textview_center:
                mViewPager.setCurrentItem(CENTER,false);
                break;
            case R.id.textview_scan:
                //只有点击扫描的时候是启动一个新的界面去扫描
                break;
        }
    }
}
