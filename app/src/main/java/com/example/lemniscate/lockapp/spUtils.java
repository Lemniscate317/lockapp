package com.example.lemniscate.lockapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Lemniscate on 2017/4/8.
 */
public class spUtils {
    public static final String configname = "appconfig";
    public static final String name = "appname";

    public static void setAppName(Context context, String appname) {
        List<String> list = getALLAppName(context);
        list.add(appname);
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        stringBuilder.subSequence(0, stringBuilder.length() - 1);
        Log.e("setAppName", stringBuilder.toString());
        getsp(context).edit().putString(name, stringBuilder.toString()).commit();
    }

    public static void removeAppName(Context context, String appname) {
        List<String> list = getALLAppName(context);
        boolean isRemove = list.remove(appname);
        if (isRemove) {
            if (list.size() != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String s : list) {
                    stringBuilder.append(s);
                    stringBuilder.append(",");
                }
                stringBuilder.subSequence(0, stringBuilder.length() - 1);
                Log.e("setAppName", stringBuilder.toString());
                getsp(context).edit().putString(name, stringBuilder.toString()).commit();
            }else {
                Log.e("setAppName", "");
                getsp(context).edit().putString(name, "").commit();
            }
        }
    }


    private static SharedPreferences getsp(Context context) {
        return context.getSharedPreferences(configname, Context.MODE_PRIVATE);
    }

    public static List<String> getALLAppName(Context context) {
        String string = getsp(context).getString(name, "");
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split(",");
            for (String name : split) {
                list.add(name);
            }
            Log.i("getALLAppName", string);
            return list;
        }
        return list;
    }
}
