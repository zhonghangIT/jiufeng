package com.uniquedu.cemetery.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniquedu.cemetery.BaseFragment;
import com.uniquedu.cemetery.R;

/**
 * Created by ZhongHang on 2016/3/4.
 */
public class CenterFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_center,null);
        return view;
    }
}
