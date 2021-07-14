package com.example.fitness

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


open class MyPreferenses(private var context: Context) {

    private var instance: MyPreferenses? = null
    private var sharedPreferences: SharedPreferences? = null
    var list: List<Class<*>>? = ArrayList<Class<*>>()
    private val IS_OTP_SET = "IS_OTP_SET"
    private val IS_FIRST_TIME = "IS_FIRST_TIME"

    init {
        this.context = context
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }


    fun clear() {
        val sharedPrefs = getDefaultSharedPreferences(context)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.clear()
        editor.commit()
    }

    fun getInt(key: String?, def: Int): Int? {
        return sharedPreferences?.getInt(key, def)
    }

    fun getString(key: String?, def: String?): String? {
        return sharedPreferences?.getString(key, def)
    }

    fun getBoolean(key: String?, def: Boolean): Boolean? {
        return sharedPreferences?.getBoolean(key, def)
    }

    fun getFloat(key: String?, def: Float): Float? {
        return sharedPreferences?.getFloat(key, def)
    }

    fun getDouble(key: String?, def: Double): Double? {
        return sharedPreferences?.getString(key, def.toString())?.toDouble()
    }

    fun getLong(key: String?, def: Long): Long? {
        return sharedPreferences?.getLong(key, def)
    }

    fun setList(key: String?, list: List<Class<*>?>?) {
        val e: SharedPreferences.Editor? = sharedPreferences?.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        if (e != null) {
            e.putString(key, json)
            e.apply()
            e.commit()
        }
    }

    fun getList(key: String?): List<Class<*>>? {
        val gson = Gson()
        try {
            val json: String? = sharedPreferences?.getString(key, "")
            if (list == null) {
                list = ArrayList<Class<*>>()
            } else {
                list = gson.fromJson<List<Class<*>>>(
                    json,
                    object : TypeToken<ArrayList<Class<*>?>?>() {}.getType()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun setInt(key: String?, `val`: Int) {
        val e: SharedPreferences.Editor? = sharedPreferences?.edit()
        if (e != null) {
            e.putInt(key, `val`)
            e.apply()
            e.commit()
        }
    }

    fun setString(key: String?, `val`: String?) {
        val e: SharedPreferences.Editor? = sharedPreferences?.edit()
        if (e != null) {
            e.putString(key, `val`)
            e.apply()
            e.commit()
        }
    }

    fun setBoolean(key: String?, `val`: Boolean) {
        val e: SharedPreferences.Editor? = sharedPreferences?.edit()
        if (e != null) {
            e.putBoolean(key, `val`)
            e.apply()
            e.commit()
        }
    }

    fun setFloat(key: String?, `val`: Float) {
        val e: SharedPreferences.Editor? = sharedPreferences?.edit()
        if (e != null) {
            e.putFloat(key, `val`)
            e.apply()
            e.commit()
        }
    }

    fun setDouble(key: String?, `val`: Double) {
        val e: SharedPreferences.Editor? =
            sharedPreferences?.edit()
        if (e != null) {
            e.putString(key, `val`.toString())
            e.apply()
            e.commit()
        }
    }

    fun setLong(key: String?, `val`: Long) {
        val e: SharedPreferences.Editor? = sharedPreferences?.edit()
        if (e != null) {
            e.putLong(key, `val`)
            e.apply()
            e.commit()
        }
    }

    val isOtpSet: Boolean
        get() = getBoolean(IS_OTP_SET, false) == true

    fun setOtp(isOtp: Boolean) {
        setBoolean(IS_OTP_SET, isOtp)
    }

    var isFirstTime: Boolean
        get() = getBoolean(IS_FIRST_TIME, true) == true
        set(isFirstTime) {
            setBoolean(IS_FIRST_TIME, isFirstTime)
        }
}