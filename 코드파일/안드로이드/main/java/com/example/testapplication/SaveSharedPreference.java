package com.example.testapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class SaveSharedPreference {

    static final String PREF_USER_ID = "userid";
    static final String PREF_KEYWORD_1 = "keyword1";
    static final String PREF_KEYWORD_2 = "keyword2";
    static final String PREF_KEYWORD_3 = "keyword3";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    // 계정 정보 저장
    public static void setUserInfo(Context ctx, String userid, String keyword1, String keyword2, String keyword3) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, userid);
        editor.putString(PREF_KEYWORD_1,keyword1);
        editor.putString(PREF_KEYWORD_2,keyword2);
        editor.putString(PREF_KEYWORD_3,keyword3);
        editor.commit();
    }

    // 계정 정보 저장
    public static void setUserKeyword(Context ctx, String keyword1, String keyword2, String keyword3) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_KEYWORD_1,keyword1);
        editor.putString(PREF_KEYWORD_2,keyword2);
        editor.putString(PREF_KEYWORD_3,keyword3);
        editor.commit();
    }

    // 저장된 정보 가져오기
    public static String getUserID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "");
    }

    // 저장된 정보 가져오기
    public static String getPrefKeyword1(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_KEYWORD_1, "");
    }

    // 저장된 정보 가져오기
    public static String getPrefKeyword2(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_KEYWORD_2, "");
    }

    // 저장된 정보 가져오기
    public static String getPrefKeyword3(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_KEYWORD_3, "");
    }

    // 로그아웃
    public static void clearUserInfo(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
