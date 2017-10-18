package com.zjj.daggerdatabinding.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by zjj on 17/10/17.
 * 自定义存一些关键值
 */
object PreUtils {


    private fun getSharedPreferences(context: Context): SharedPreferences {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun isFirstTime(context: Context, key: String): Boolean {
        if (getBoolean(context, key, false)) {
            return false
        } else {
            putBoolean(context, key, true)
            return true
        }
    }

    fun contains(context: Context, key: String): Boolean {
        return PreUtils.getSharedPreferences(context).contains(key)
    }

    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        return PreUtils.getSharedPreferences(context).getInt(key, defaultValue)
    }

    fun putInt(context: Context, key: String, pValue: Int): Boolean {
        val editor = PreUtils.getSharedPreferences(context).edit()

        editor.putInt(key, pValue)

        return editor.commit()
    }

    fun getLong(context: Context, key: String, defaultValue: Long): Long {
        return PreUtils.getSharedPreferences(context).getLong(key, defaultValue)
    }

    fun getLong(context: Context, key: String, defaultValue: Long?): Long? {
        if (PreUtils.getSharedPreferences(context).contains(key)) {
            return PreUtils.getSharedPreferences(context).getLong(key, 0)
        } else {
            return null
        }
    }


    fun putLong(context: Context, key: String, pValue: Long): Boolean {
        val editor = PreUtils.getSharedPreferences(context).edit()

        editor.putLong(key, pValue)

        return editor.commit()
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return PreUtils.getSharedPreferences(context).getBoolean(key, defaultValue)
    }

    fun putBoolean(context: Context, key: String, pValue: Boolean): Boolean {
        val editor = PreUtils.getSharedPreferences(context).edit()

        editor.putBoolean(key, pValue)

        return editor.commit()
    }

    fun getString(context: Context, key: String, defaultValue: String): String {
        return PreUtils.getSharedPreferences(context).getString(key, defaultValue)
    }

    fun putString(context: Context, key: String, pValue: String): Boolean {
        val editor = PreUtils.getSharedPreferences(context).edit()

        editor.putString(key, pValue)

        return editor.commit()
    }


    fun remove(context: Context, key: String): Boolean {
        val editor = PreUtils.getSharedPreferences(context).edit()

        editor.remove(key)

        return editor.commit()
    }

}