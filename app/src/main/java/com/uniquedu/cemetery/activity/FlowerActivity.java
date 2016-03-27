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
public class FlowerActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private GridView mFlower_grid;
    private int [] flower_image = {R.mipmap.single1,R.mipmap.single2,R.mipmap.single3,R.mipmap.single4,
            R.mipmap.single5,R.mipmap.single6,R.mipmap.single7,R.mipmap.single8};
    private String [] flower_name = {"爱的纪念","永远怀念","千里寄相思","无尽的爱","永恒的思念",
            "真切的思念","追忆","思念款款"};
    private TextView mFlower_back,mFlower_head;
    private Button mFlower_complete;
    private EditText mFlower_name,mFlower_title,mFlower_content;
    private String id;
    private String mFlower_id = "0";
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
        mFlower_head.setText("献花");
        mFlower_back = (TextView) findViewById(R.id.flower_back);
        mFlower_complete = (Button) findViewById(R.id.flower_complete);
        mFlower_name = (EditText) findViewById(R.id.flower_name);
        mFlower_title = (EditText) findViewById(R.id.flower_title);
        mFlower_content = (EditText) findViewById(R.id.flower_content);
        mFlower_back.setOnClickListener(this);
        mFlower_complete.setOnClickListener(this);
        mFlower_grid = (GridView) findViewById(R.id.flower_grid);
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for (int i = 0; i < flower_image.length; i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("image",flower_image[i]);
            map.put("text",flower_name[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.item_flwoer,
                new String[]{"image","text"},new int[]{R.id.flower_image,R.id.flower_name});
        mFlower_grid.setAdapter(adapter);
        mFlower_grid.setOnItemClickListener(this);
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
                if (name.length() == 0 || title.length() == 0 || content.length() == 0 || mFlower_id == "0"){
                    Toast.makeText(mContext,"请输入姓名，标题和留言",Toast.LENGTH_SHORT).show();
                }else if(name.length() != 0 && title.length() != 0 && content .length() != 0 && mFlower_id != "0"){
                    mRequestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            Address.WONSHIP_THEME + id + "&user=" + name + "&title=" + title + "&content=" + content + "&actiontype=" + 5 + "&typenum="+ mFlower_id,
                            new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(mContext,"献花成功",Toast.LENGTH_SHORT).show();
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
                mFlower_id = 1+"";
                break;
            case 1:
                mFlower_id = 3+"";
                break;
            case 2:
                mFlower_id = 3+"";
                break;
            case 3:
                mFlower_id = 4+"";
                break;
            case 4:
                mFlower_id = 5+"";
                break;
            case 5:
                mFlower_id = 6+"";
                break;
            case 6:
                mFlower_id = 7+"";
                break;
            case 7:
                mFlower_id = 8+"";
                break;
        }

    }
}
