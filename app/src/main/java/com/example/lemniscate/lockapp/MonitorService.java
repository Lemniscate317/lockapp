package com.example.lemniscate.lockapp;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MonitorService extends Service {
    private Context mContext;
    private Timer mTimer;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mTimer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                getTopApp();
            }
        };
        mTimer.schedule(task, 1000, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
    }

    private boolean isAllow(String topname) {
        boolean isALlow = false;
        List<String> list = spUtils.getALLAppName(this);
        for (String name : list) {
            if (name.equals(topname)) {
                isALlow = true;
                break;
            }
        }
        return isALlow;
    }


    private void getTopApp() {
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);//usagestats
        long time = System.currentTimeMillis();
        List<UsageStats> usageStatsList = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            usageStatsList = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time - 2000, time);

            if (usageStatsList != null && !usageStatsList.isEmpty()) {
                SortedMap<Long, UsageStats> usageStatsMap = new TreeMap<>();
                for (UsageStats usageStats : usageStatsList) {
                    usageStatsMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!usageStatsMap.isEmpty()) {
                    String topPackageName = usageStatsMap.get(usageStatsMap.lastKey()).getPackageName();

                    if (getLauncherPackageName(mContext).equals(topPackageName) || this.getPackageName().equals(topPackageName)) {
                        return;
                    }
                    if (isAllow(topPackageName)) {
                        return;
                    }

                    Log.e("TopPackage Name", topPackageName);

                    //模拟home键点击
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(intent);

                    //启动提示页面
                    Intent intent1 = new Intent(mContext, TipActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                }
            }
        }

    }

    public String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }



}
