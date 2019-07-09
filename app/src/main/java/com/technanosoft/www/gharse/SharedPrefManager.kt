package com.technanosoft.www.gharse

import android.text.method.TextKeyListener.clear
import android.R.id.edit
import android.content.Context
import android.content.SharedPreferences.Editor
import android.content.SharedPreferences
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.flags.ModuleDescriptor
import com.technanosoft.www.gharse.Models.Tiffin_Sys


class SharedPrefManager {
    private val KEY_RegisterID = "RegisterID_data"
    private val KEY_Name = "Name_data"
    private val KEY_Email = "Email_data"
    private val KEY_Mobile = "Mobile_data"
    private val KEY_Gender = "Gender_data"
    private val KEY_Status = "Status_data"
    private val KEY_RegistrationType = "RegistrationType_data"
    private val KEY_Latitude = "Latitude_data"
    private val KEY_Longitude = "Longitude_data"
    private val SHARED_PREF_NAME = "Gharse_app"
    private val SHARED_PREF_TIFFIN = "Gharse_app_Tiffin"
    private val weeki = "WeekID"
    private val OtpUp = "OtpUp"
    private val OtpPass = "OtpPass"
    private val KEY_FOOD = "FOODTYP"
    private var mCtx: Context? = null
    private var mInstance: SharedPrefManager? = null

    fun SharedPrefManager_fun(context: Context) {
        mCtx = context
    }

    @Synchronized
    fun getInstance(context: Context): SharedPrefManager {
        var pref:SharedPrefManager
        synchronized(SharedPrefManager::class.java) {
            if (mInstance == null) {
                mInstance = SharedPrefManager()
            }
            pref = mInstance!!
        }
        return pref
    }

    fun userStartLogin(user: User) {
        val edit = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, 0).edit()
        edit.putString(KEY_Email, user.Email)
        edit.apply()
    }

    fun userLogin(user: User) {
        val edit = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, 0).edit()
        edit.putInt(KEY_RegisterID, user.RegisterID)
        edit.putString(KEY_Name, user.Name)
        edit.putString(KEY_Email, user.Email)
        edit.putString(KEY_Mobile, user.Mobile)
        edit.putString(KEY_Gender, user.Gender)
        edit.putString(KEY_Status, user.Status)
        edit.putString(KEY_RegistrationType, user.RegistrationType)
        edit.putString(KEY_Latitude, user.Latitude)
        edit.putString(KEY_Longitude, user.Longitude)
        edit.apply()
    }

    fun isLoggedIn(type:String): Boolean {
        return if (mCtx!!.getSharedPreferences(SHARED_PREF_NAME, 0).getInt(KEY_RegisterID, -1) > -1 && mCtx!!.getSharedPreferences(SHARED_PREF_NAME, 0).getString(KEY_RegistrationType,"").toLowerCase() == type.toLowerCase()) {
            true
        } else {
            false
        }
    }

    fun getUser(): User {
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, 0)
        return User(
            sharedPreferences.getInt(KEY_RegisterID, -1),
            sharedPreferences.getString(KEY_Name, null),
            sharedPreferences.getString(KEY_Email, null),
            sharedPreferences.getString(KEY_Mobile, null),
            sharedPreferences.getString(KEY_Gender, null),
            sharedPreferences.getString(KEY_Status,null),
            sharedPreferences.getString(KEY_RegistrationType,null),
            sharedPreferences.getString(KEY_Latitude,null),
            sharedPreferences.getString(KEY_Longitude,null)
        )
    }

    fun logout() {
        val edit = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, 0).edit()
        edit.clear()
        edit.apply()
    }
    fun putweekId(Tiff:Tiffin_Sys){
        val edit = mCtx!!.getSharedPreferences(SHARED_PREF_TIFFIN, 0).edit()
        edit.putString(weeki,Tiff.weekid)
        edit.putString(KEY_FOOD,Tiff.foodtime)
        edit.apply()
    }
    fun getWeekID():Tiffin_Sys{
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_TIFFIN, 0)
        val week = sharedPreferences.getString(weeki,null)
        return Tiffin_Sys(week!!,sharedPreferences.getString(KEY_FOOD,null)!!)
    }
    fun delWeek(){
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_TIFFIN, 0)
        sharedPreferences.edit().clear().apply()
    }
    fun otpup(Str:String,Passw:String){
        val edit = mCtx!!.getSharedPreferences(SHARED_PREF_TIFFIN, 0).edit()
        edit.putString(OtpUp,Str)
        edit.putString(OtpPass,Passw)
        edit.apply()
    }
    fun otview():String?{
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_TIFFIN, 0)
        val update = sharedPreferences.getString(OtpUp,null)
        return update!!
    }
    fun password():String{
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_TIFFIN, 0)
        val update = sharedPreferences.getString(OtpPass,null)
        return update!!
    }
}