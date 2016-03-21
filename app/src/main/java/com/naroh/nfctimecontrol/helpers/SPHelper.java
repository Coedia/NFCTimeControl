/*
 * Copyright (c) 2014. David Alejandro Fernández Sancho.
 * All rights reserved.
 */

package com.naroh.nfctimecontrol.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SPHelper {

    public static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putBoolean(Context context, String key, boolean val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public static void putString(Context context, String key, String val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.putString(key, val);
        edit.commit();
    }

    public static void putFloat(Context context, String key, float val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.putFloat(key, val);
        edit.commit();
    }

    public static void putLong(Context context, String key, long val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.putLong(key, val);
        edit.commit();
    }

    public static void clear(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = preferences.edit();
        edit.clear();
        edit.commit();
    }

    public static long getLong(Context context, String key, long _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, _default);
    }

    public static float getFloat(Context context, String key, float _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getFloat(key, _default);
    }

    public static String getString(Context context, String key, String _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, _default);
    }

    public static int getInt(Context context, String key, int _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, _default);
    }

    public static boolean getBoolean(Context context, String key, boolean _default) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, _default);
    }

    public static long getDefaultUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("userSeleccionado", -1);
    }

    public static void putDefaultUser(Context context, long val) {
        putLong(context, "userSeleccionado", val);
    }


}
