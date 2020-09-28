package com.example.hurui.news.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker

object PermissionUtil {

    const val CHECK_REQUEST_PERMISSION_RESULT = 3

    fun hasPermission(context: Context, permissions: Array<String>) : Boolean{
        if (permissions == null || permissions.isEmpty()){
            return true
        }
        for (per in permissions){
            if (ContextCompat.checkSelfPermission(context, per) != PermissionChecker.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int){
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

}