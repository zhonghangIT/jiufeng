package com.uniquedu.cemetery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.uniquedu.cemetery.Address;
import com.uniquedu.cemetery.BaseActivity;
import com.uniquedu.cemetery.R;

/**
 * Created by Administrator on 2016/3/17.
 */
public class CleanActivity extends BaseActivity implements View.OnClickListener {
    private TextView mGrid_back, mGrid_head;
    private Button mComplete;
    private EditText mStyle_name, mStyle_title, mStyle_content;
    private String id;
    private Context mContext;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worship);
        mContext = this;
        infoID();
        Bundle bundle = getIntent().getBundleExtra("id");
        id = bundle.getString("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    private void infoID() {
        mGrid_head = (TextView) findViewById(R.id.grid_head);
        mGrid_head.setText("清洁");
        mGrid_back = (TextView) findViewById(R.id.grid_back);
        mComplete = (Button) findViewById(R.id.complete);
        mStyle_name = (EditText) findViewById(R.id.style_name);
        mStyle_title = (EditText) findViewById(R.id.style_title);
        mStyle_content = (EditText) findViewById(R.id.style_content);
        mGrid_back.setOnClickListener(this);
        mComplete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_back:
                finish();
                break;
            case R.id.flower_complete:
                String name = mStyle_name.getText().toString();
                String title = mStyle_title.getText().toString();
                String content = mStyle_content.getText().toString();
                if (name.length() == 0 || title.length() == 0 || content.length() == 0) {
                    Toast.makeText(mContext, "请输入姓名，标题和留言", Toast.LENGTH_SHORT).show();
                } else {
                    mRequestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            Address.WONSHIP_THEME + id + "&user=" + name + "&title=" + title + "&content=" + content + "&actiontype=" + 3 + "&typenum=" + 0, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(mContext, "跪拜成功", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("TAG", volleyError.toString());
                        }
                    });

                    mRequestQueue.add(stringRequest);

                    //这里使用了singleTask的启动模式，会自动关闭以上界面
                    Intent intent = new Intent(getApplicationContext(), DeadHomePageActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

}
