package com.example.lemniscate.lockapp;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorlayout;
    private RecyclerView rv;
    private MultiTypeAdapter adapter;

    private FloatingActionButton fab;
    private FloatingActionButton fabService;
    private Items infos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        initToolbar();
        initView();
        initData();
    }


    private void initData() {
        //rv
        infos = new Items();
        Utils.getAllProgramInfo(infos, this, Utils.DEFAULT);
        adapter = new MultiTypeAdapter();
        adapter.register(Appbean.class, new AppViewProvider());
        adapter.register(Text.class, new TextViewProvier());
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);

        infos.add(1, new Text("权限状态  :" + (checkPermission() ? "已获得权限" : "未获得权限")));
        infos.add(1, new Text("服务开启状态 " + (Utils.isServiceWork(MainActivity.this, MonitorService.class.getCanonicalName()) ? "服务已开启" : "服务未开启")));
        infos.add(1, new Text("选择可以使用的应用 双击状态栏可回到上部"));
        adapter.setItems(infos);

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    openPermission();
                }
            }
        });
        fabService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    boolean isService = Utils.isServiceWork(MainActivity.this, MonitorService.class.getCanonicalName());
                    if (isService) {
                        stopService(new Intent(MainActivity.this, MonitorService.class));
                        Toast.makeText(MainActivity.this, "服务已关闭！", Toast.LENGTH_SHORT).show();
                    } else {
                        startService(new Intent(MainActivity.this, MonitorService.class));
                        Toast.makeText(MainActivity.this, "服务已开启！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "点击基佬紫给lockapp开启权限再打开服务", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AppbeanMessageEvent event) {
        if (infos != null) {
            Appbean bean = (Appbean) infos.get(event.position);
            bean.isCheck = event.isCheck;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }


    private void initView() {
        coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabService = (FloatingActionButton) findViewById(R.id.fabService);

        rv = (RecyclerView) findViewById(R.id.rv);
    }

    private void openPermission() {
        if (!checkPermission()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, 100);
            }
        }
    }

    private void initToolbar() {
        MyToolbar toolbar = (MyToolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("lock something");
        toolbar.OnTwoTapListener(new MyToolbar.OnTwoTapListener() {
            @Override
            public void onTwoTap() {
                if (rv != null) {
                    rv.smoothScrollToPosition(0);
                }
            }
        });
    }

    private boolean checkPermission() {
        AppOpsManager aom = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            int mode = aom.checkOpNoThrow("android:get_usage_stats", Process.myUid(), getPackageName());
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            if (checkPermission()) {
                Snackbar.make(coordinatorlayout, "权限已经开启", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(coordinatorlayout, "未能获得权限", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {

        }
    }
}
