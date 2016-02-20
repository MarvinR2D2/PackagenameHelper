package com.marvin.packagenamehelper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by MarvinR2D2 on 2016/2/19.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    @Bind(R.id.lv_all_apps)
    ListView allApps;
    @Bind(R.id.ll_choose_apk)
    LinearLayout chooseApk;

    @OnClick(R.id.ll_choose_apk)
    void OnButtonClick() {

    }
    @OnItemClick(R.id.lv_all_apps)
    void OnListClick(){

    }

    private AppInfoProvider mInfoProvider;
    private List<AppInfo> infoList = new ArrayList<>();
    private AppAdapter mAdapter;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mAdapter = new AppAdapter();
                    allApps.setAdapter(mAdapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getAllAppInfos();
        allApps.setOnItemClickListener(this);
    }

    private void getAllAppInfos() {
        mInfoProvider = new AppInfoProvider(MainActivity.this);
        new Thread() {
            @Override
            public void run() {
                infoList = mInfoProvider.getAllApps();
                if (!handler.hasMessages(0))
                    handler.sendEmptyMessageDelayed(0, 80);
                super.run();
            }
        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        String packageName = infoList.get(i).getPackageName();
        ClipData clipData = ClipData.newPlainText("packageName",packageName);
        cm.setPrimaryClip(clipData);
        Log.e("miao","on item click .........");
        Toast.makeText(MainActivity.this,getString(R.string.alread_copy_packagename),Toast.LENGTH_SHORT).show();
    }

    public class AppAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int i) {
            return infoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            ViewHolder viewHolder;
            AppInfo item = infoList.get(position);
            String appName = item.getAppName();
            String packageName = item.getPackageName();
            Drawable icon = item.getIcon();
            if (convertview == null) {
                convertview = View.inflate(MainActivity.this, R.layout.item_app, null);
                viewHolder = new ViewHolder(convertview);
                convertview.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertview.getTag();
            }
                viewHolder.icon.setImageDrawable(icon);
                viewHolder.appName.setText(appName);
                viewHolder.packageName.setText(packageName);
            return convertview;
        }


    }
        static class ViewHolder {
            @Bind(R.id.iv_icon)ImageView icon;
            @Bind(R.id.tv_app_name)TextView appName;
            @Bind(R.id.tv_package_name)TextView packageName;
            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }


}
