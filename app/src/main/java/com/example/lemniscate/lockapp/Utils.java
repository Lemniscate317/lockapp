package com.example.lemniscate.lockapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class Utils {
    public static final int DEFAULT = 0; // 默认 所有应用
    public static final int SYSTEM_APP = DEFAULT + 1; // 系统应用
    public static final int NONSYSTEM_APP = DEFAULT + 2; // 非系统应用

    public static  boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(1000);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    public static Items getAllNonsystemProgramInfo(Context context) {
        Items items = new Items();
//        List<Appbean> nonsystemAppList = new ArrayList<Appbean>();
        getAllProgramInfo(items, context, NONSYSTEM_APP);
        return items;
    }

    public static void getAllProgramInfo(Items applist,
                                         Context context, int type) {
//        ArrayList<Appbean> appList = new ArrayList<Appbean>(); // 用来存储获取的应用信息数据
        List<String> allAppName = spUtils.getALLAppName(context);

        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            Appbean tmpInfo = new Appbean();
            tmpInfo.name = packageInfo.applicationInfo.packageName;
//            tmpInfo.packageName = packageInfo.packageName;
//            tmpInfo.versionName = packageInfo.versionName;
//            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.src = packageInfo.applicationInfo.loadIcon(context
                    .getPackageManager());
            if (allAppName.contains(tmpInfo.name)) {
                tmpInfo.isCheck = true;
            }
            switch (type) {
                case NONSYSTEM_APP:
                    if (!isSystemAPP(packageInfo)) {
                        applist.add(tmpInfo);
                    }
                    break;
                case SYSTEM_APP:
                    if (isSystemAPP(packageInfo)) {
                        applist.add(tmpInfo);
                    }
                    break;
                default:
                    applist.add(tmpInfo);
                    break;
            }

        }
    }

    public static Boolean isSystemAPP(PackageInfo packageInfo) {
        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // 非系统应用
            return false;
        } else { // 系统应用
            return true;
        }
    }
}
