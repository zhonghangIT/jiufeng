package com.uniquedu.cemetery.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ThouActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private GridView mThou_grid;
    private int [] thou_image = {R.mipmap.thus_red,R.mipmap.thus_forefather,R.mipmap.thus_god};
    private String [] thou_name = {"红色高香","祭祖高香","灵香"};
    private TextView mFlower_back,mFlower_head;
    private Button mFlower_complete;
    private EditText mFlower_name,mFlower_title,mFlower_content;
    private String id;
    private String mThou_id = "0";
    private Context mContext;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);
        mContext = this;
        infoID();
        Bundle bundle = getIntent().getBundleExtra("id");
        id = bundle.getString("id");
    }
    private void infoID() {
        mFlower_head = (TextView) findViewById(R.id.flower_head);
        mFlower_head.setText("上香");
        mFlower_back = (TextView) findViewById(R.id.flower_back);
        mFlower_complete = (Button) findViewById(R.id.flower_complete);
        mFlower_name = (EditText) findViewById(R.id.flower_name);
        mFlower_title = (EditText) findViewById(R.id.flower_title);
        mFlower_content = (EditText) findViewById(R.id.flower_content);
        mFlower_back.setOnClickListener(this);
        mFlower_complete.setOnClickListener(this);
        mThou_grid = (GridView) findViewById(R.id.flower_grid);
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for (int i = 0; i < thou_image.length; i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("image",thou_image[i]);
            map.put("text",thou_name[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.item_flwoer,
                new String[]{"image","text"},new int[]{R.id.flower_image,R.id.flower_name});
        mThou_grid.setAdapter(adapter);
        mThou_grid.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.flower_back:
                finish();
                break;
            case R.id.flower_complete:
                String name = mFlower_name.getText().toString();
                String title = mFlower_title.getText().toString();
                String content = mFlower_content.getText().toString();
                if (name.length() == 0 || title.length() == 0 || content.length() == 0){
                    Toast.makeText(mContext,"请输入姓名，标题和留言",Toast.LENGTH_SHORT).show();
                }else if(name.length() != 0 && title.length() != 0 && content .length() != 0 && mThou_id != "0"){
                    mRequestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            Address.WONSHIP_THEME + id + "&user=" + name + "&title=" + title + "&content=" + content + "&actiontype=" + 4 + "&typenum="+ mThou_id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Toast.makeText(mContext,"上香成功",Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e("TAG", volleyError.toString());
                        }
                    });

                    mRequestQueue.add(stringRequest);

                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                mThou_id = 1+"";
                break;
            case 1:
                mThou_id = 3+"";
                break;
            case 2:
                mThou_id = 3+"";
                break;

        }

    }
}

