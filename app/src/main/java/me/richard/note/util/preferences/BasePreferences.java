package me.richard.note.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import me.richard.note.PalmApp;
import me.richard.note.util.LogUtils;

/**
 * Created by Richard on 2018/3/3. */
public class BasePreferences {

    private static SharedPreferences mPreferences;

    public BasePreferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        mPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    protected String getKey(@StringRes int resId) {
        return PalmApp.getStringCompact(resId);
    }

    protected String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    protected void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    protected int getInt(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    protected void putInt(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }

    protected long getLong(String key, long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    protected void putLong(String key, long value) {
        mPreferences.edit().putLong(key, value).apply();
    }

    protected boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    protected void putBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    protected void putStringSet(String key, Set<String> stringSet) {
        mPreferences.edit().putStringSet(key, stringSet).apply();
    }

    protected Set<String> getStringSet(String key, Set<String> defaultStringSet) {
        return mPreferences.getStringSet(key, defaultStringSet);
    }

    protected String getString(@StringRes int keyResId, String defaultValue) {
        return mPreferences.getString(getKey(keyResId), defaultValue);
    }

    protected void putString(@StringRes int keyResId, String value) {
        mPreferences.edit().putString(getKey(keyResId), value).apply();
    }

    protected int getInt(@StringRes int keyResId, int defaultValue) {
        return mPreferences.getInt(getKey(keyResId), defaultValue);
    }

    protected void putInt(@StringRes int keyResId, int value) {
        mPreferences.edit().putInt(getKey(keyResId), value).apply();
    }

    protected long getLong(@StringRes int keyResId, long defaultValue) {
        return mPreferences.getLong(getKey(keyResId), defaultValue);
    }

    protected void putLong(@StringRes int keyResId, long value) {
        mPreferences.edit().putLong(getKey(keyResId), value).apply();
    }

    protected boolean getBoolean(@StringRes int keyResId, boolean defaultValue) {
        return mPreferences.getBoolean(getKey(keyResId), defaultValue);
    }

    protected void putBoolean(@StringRes int keyResId, boolean value) {
        mPreferences.edit().putBoolean(getKey(keyResId), value).apply();
    }

    protected void putStringSet(@StringRes int keyResId, Set<String> stringSet) {
        mPreferences.edit().putStringSet(getKey(keyResId), stringSet).apply();
    }

    protected Set<String> getStringSet(@StringRes int keyResId, Set<String> defaultStringSet) {
        return mPreferences.getStringSet(getKey(keyResId), defaultStringSet);
    }

    /**
     * 保存对象
     * @param object
     */
    protected <T> void setObject(@StringRes int keyResId , Object object) {
        SharedPreferences.Editor editor = mPreferences.edit();
        Gson gson = new Gson();
        //为空不能返回，必须可以设置为空
        if (object == null){
            editor.putString(getKey(keyResId), "");
            editor.commit();
            return;
        }
        //转换成json数据，再保存
        String strJson = gson.toJson(object);
        editor.putString(getKey(keyResId), strJson);
        editor.commit();
    }
    /**
     *
     * @param keyResId
     * @param <T>
     */
    protected  <T> void deleteObject(@StringRes int keyResId) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(getKey(keyResId), null);
        editor.commit();
    }

    /**
     * 获取对象信息
     * @param type
     * @return
     */
    protected   <T> T getObject(@StringRes int keyResId,Class<T> type) {

        String strJson = mPreferences.getString(getKey(keyResId),"");

        if (null == strJson) {
            return null;
        }
        Gson gson = new Gson();
        T object = gson.fromJson(strJson, type);
        return object;

    }
    /**
     * 保存对象列表
     * @param datalist
     */
    protected  <T> void setObjectList(@StringRes int keyResId, List<T> datalist) {
        if (null == datalist){
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(getKey(keyResId), strJson);
        editor.commit();
    }
    /**
     * 获取对象列表
     * @return getObjectList(getBookMarksKey(bookId),  BookMark[].class);
     */
    protected   <T> List<T> getObjectList(@StringRes int keyResId, Class<T[]> type) {
        List<T> datalist=new ArrayList<T>();
        try {
            String strJson = mPreferences.getString(getKey(keyResId),"");
            if ("".equals(strJson)) {
                return datalist;
            }
            Gson gson = new Gson();
            T[] list = gson.fromJson(strJson, type);
            datalist.addAll(Arrays.asList(list));
        }catch (Exception e){
            LogUtils.i("本地json解析错误");
        }
        return datalist;
    }
}
