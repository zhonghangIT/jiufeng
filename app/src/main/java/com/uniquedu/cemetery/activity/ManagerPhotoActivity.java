package com.uniquedu.cemetery.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.PopupWindowCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.uniquedu.cemetery.R;
import com.uniquedu.cemetery.adapter.PhotoAdapter;
import com.uniquedu.cemetery.bean.PhotoBean;
import com.uniquedu.cemetery.bean.SignIn;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshBase;
import com.uniquedu.cemetery.pulltorefresh.PullToRefreshListView;
import com.uniquedu.cemetery.utils.BitmapUtils;
import com.uniquedu.cemetery.utils.PostFileRequest;
import com.uniquedu.cemetery.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.core.ViewFinderView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by ZhongHang on 2016/4/26.
 */
public class ManagerPhotoActivity extends BaseActivity implements View.OnClickListener {
    private ListView mListView;
    private PullToRefreshListView mPullRefreshView;
    private LayoutInflater mInflater;
    private String deadId;
    private ArrayList<PhotoBean> mList;
    private int page = 1;
    private PhotoAdapter mAdapter;
    private static final int STATE_LOAD_MORE = 0;
    private static final int STATE_LOAD_REFRESH = 1;
    private TextView mTextViewNull;
    private TextView mTextViewBack;
    private ImageView mImageViewEdit;
    private ImageView mImageViewCamera;
    private TextView mTextViewHead;
    private TextView mTextViewCancel;
    private ImageView mImageViewDelete;
    private SignIn signIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_photo);
        mTextViewBack = (TextView) findViewById(R.id.grid_back);
        mTextViewHead = (TextView) findViewById(R.id.grid_head);
        mTextViewCancel = (TextView) findViewById(R.id.textview_cancel);
        mImageViewDelete = (ImageView) findViewById(R.id.imageview_delete);
        mTextViewCancel.setOnClickListener(this);
        mImageViewDelete.setOnClickListener(this);
        mTextViewBack.setOnClickListener(this);
        mImageViewEdit = (ImageView) findViewById(R.id.imageview_edit);
        mImageViewEdit.setOnClickListener(this);
        mImageViewCamera = (ImageView) findViewById(R.id.imageview_camera);
        mImageViewCamera.setOnClickListener(this);
        Gson gson = new Gson();
        signIn = gson.fromJson(SharedPreferencesUtil.getData(getApplicationContext(), "signIn", "").toString(), SignIn.class);

        deadId = getIntent().getStringExtra("deadId");
        mInflater = getLayoutInflater();
        mPullRefreshView = (PullToRefreshListView) findViewById(R.id.photo_listview);
        mListView = mPullRefreshView.getRefreshableView();
        mTextViewNull = (TextView) findViewById(R.id.textview_null);
        mList = new ArrayList<>();
        mAdapter = new PhotoAdapter(mInflater, mList);
        mListView.setAdapter(mAdapter);
        mPullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(STATE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(STATE_LOAD_MORE);
            }
        });
        loadData(STATE_LOAD_REFRESH);
        mAdapter.setOnItemListener(new PhotoAdapter.OnItemClick() {
            @Override
            public void onItemClick(PhotoBean bean) {
                Intent intent = new Intent(getApplicationContext(), ShowPhotoActivity.class);
                intent.putExtra("photo", bean);
                startActivity(intent);
            }
        });
    }

    private void loadData(int state) {
        if (state == STATE_LOAD_MORE) {
            page++;
        } else {
            page = 1;
        }
        HashMap<String, String> loadmore = new HashMap<>();
        loadmore.put("callback", "callbakename");
        loadmore.put("page", "" + page);
        loadmore.put("rows", "12");
        loadmore.put("id", deadId);
        getPhotos(loadmore, state);
    }

    private void getPhotos(final HashMap<String, String> params, final int state) {
        if (deadId != null) {
            StringRequest request = new StringRequest(Request.Method.POST, Address.WORSHIP_PHOTO, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("返回照片信息", response);
                    List<PhotoBean> list = resloveData(response);
                    if (state == STATE_LOAD_MORE) {
                        if (list.size() != 0) {
                            mList.addAll(list);
                            mAdapter.setPhotos(mList);
                        } else {
                            Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_LONG).show();
                        }
                        mPullRefreshView.onRefreshComplete();
                    } else {
                        if (list.size() != 0) {
                            mList.clear();
                            mList.addAll(list);
                            mAdapter.setPhotos(mList);
                            mPullRefreshView.setVisibility(View.VISIBLE);
                            mTextViewNull.setVisibility(View.INVISIBLE);
                        } else {
                            mTextViewNull.setVisibility(View.VISIBLE);
                            mPullRefreshView.setVisibility(View.INVISIBLE);
                            //显示无数据
                        }
                        mPullRefreshView.onRefreshComplete();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "获取详细信息失败", Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        } else {
            Toast.makeText(getApplicationContext(), "获取详细信息失败", Toast.LENGTH_LONG).show();
        }

    }

    private void photoEdit(boolean b) {
        if (b) {
            mTextViewHead.setVisibility(View.INVISIBLE);
            mTextViewBack.setVisibility(View.INVISIBLE);
            mImageViewCamera.setVisibility(View.INVISIBLE);
            mImageViewEdit.setVisibility(View.INVISIBLE);
            mImageViewDelete.setVisibility(View.VISIBLE);
            mTextViewCancel.setVisibility(View.VISIBLE);
        } else {
            mTextViewHead.setVisibility(View.VISIBLE);
            mTextViewBack.setVisibility(View.VISIBLE);
            mImageViewCamera.setVisibility(View.VISIBLE);
            mImageViewEdit.setVisibility(View.VISIBLE);
            mImageViewDelete.setVisibility(View.INVISIBLE);
            mTextViewCancel.setVisibility(View.INVISIBLE);
        }
    }

    private List<PhotoBean> resloveData(String response) {
        List<PhotoBean> list = new ArrayList<>();
        if (TextUtils.isEmpty(response)) {
            return list;
        }
        response = response.trim();
        if (response.indexOf("callbakename") == 0) {
            response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
        }
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("rows");
            Gson gson = new Gson();
            Type type = new TypeToken<List<PhotoBean>>() {
            }.getType();
            Object fromJson2 = gson.fromJson(array.toString(), type);
            list = (List<PhotoBean>) fromJson2;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private PopupWindow popupWindow;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_back:
                finish();
                break;
            case R.id.imageview_edit:
                photoEdit(true);
                mAdapter.setEditor(true);
                break;
            case R.id.imageview_camera:
                //此处弹出popupwindow
                createPopupWindow();
                break;
            case R.id.textview_cancel:
                photoEdit(false);
                mAdapter.setEditor(false);
                break;
            case R.id.imageview_delete:
                photoEdit(false);
                mAdapter.setEditor(false);
                Toast.makeText(ManagerPhotoActivity.this, "选中照片张数" + mAdapter.getListCheckedPhoto().size(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == RESULT_LOAD_IMAGE || requestCode == RESULT_CAMERA_IMAGE)) {
            Uri selectedImage = data.getData();
            String picturePath = getImgPath(selectedImage);
            File file = new File(Environment.getExternalStorageDirectory() + "/upload.jpg");
            Toast.makeText(ManagerPhotoActivity.this, getCacheDir() + "/upload.jpg", Toast.LENGTH_SHORT).show();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BitmapUtils.zipImage(picturePath, file.getAbsolutePath());
            postFileOk("http://www.whjfs.com/mvcwebmis/nologin/AppNewMemorialPhoto", file.getAbsolutePath());
//            postFile(file.getPath());
            Log.d("MainActivity", "图片的地址：" + picturePath);
        }
    }

    private void postFileOk(String url, String filePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        File file = new File(filePath);
//        .addFormDataPart("callBack", "callback")
//                .addFormDataPart("account", signIn.getAccount())
//                .addFormDataPart("Token", signIn.getToken())
//                .addFormDataPart("photoTitle", "callback")
        url = url + "?callBack=callback&account=" + signIn.getAccount() + "&Token=" + signIn.getToken();
        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("fileData", "fileData", MultipartBody.create(MultipartBody.FORM, file)).build();

        okhttp3.Request request = new okhttp3.Request.Builder().post(body).url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("", "网络连接返回数据错误");
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String result = response.body().string();
                Log.d("ManagerPhotoActivity", "网络连接返回数据" + result);
//                Toast.makeText(ManagerPhotoActivity.this, "上传返回的信息" + response.body().string(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postFile(String filePath) {
        //这里使用了自己定义的Request
        HashMap<String, File> params = new HashMap<>();
        File file = new File(filePath);
        params.put("fileData", file);
        final HashMap<String, String> stringParams = new HashMap<>();
        stringParams.put("callback", "callback");
        stringParams.put("account", signIn.getAccount());
        stringParams.put("token", signIn.getToken());
        stringParams.put("photoTitle", "");
        String url = "http://www.whjfs.com/mvcwebmis/nologin/AppNewMemorialPhoto";

        PostFileRequest request = new PostFileRequest(Request.Method.POST, url, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ManagerPhoto", "上传返回的信息" + response);
                Toast.makeText(ManagerPhotoActivity.this, "上传返回的信息" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ManagerPhoto", "连接网络错误" + error.networkResponse.statusCode);
                Toast.makeText(ManagerPhotoActivity.this, "连接网络错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return stringParams;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private String getImgPath(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private static final int RESULT_LOAD_IMAGE = 0x23;
    private static final int RESULT_CAMERA_IMAGE = 0x24;

    private void createPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.pop_select, null);
        TextView textviewCancel = (TextView) view.findViewById(R.id.textview_cancel);
        TextView textviewCamera = (TextView) view.findViewById(R.id.textview_camera);
        TextView textviewGalley = (TextView) view.findViewById(R.id.textview_galley);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.textview_cancel:
                        popupWindow.dismiss();
                        break;
                    case R.id.textview_camera: {
                        //启动系统摄像头
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, RESULT_CAMERA_IMAGE);
                        popupWindow.dismiss();
                    }
                    break;
                    case R.id.textview_galley: {
                        Intent i = new Intent(
                                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                        popupWindow.dismiss();
                    }
                    break;
                }
            }
        };
        textviewCancel.setOnClickListener(listener);
        textviewCamera.setOnClickListener(listener);
        textviewGalley.setOnClickListener(listener);
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        //产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        popupWindow.showAtLocation(findViewById(R.id.linear_content), Gravity.CENTER, 0, 0);
    }
}
