package com.example.evionotes

import android.app.Application
import android.content.ComponentName
import android.content.pm.PackageManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EvioNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 🔹 Hide the Leaks app launcher icon in debug builds
        try {
            val leakActivity = ComponentName(
                this,
                "leakcanary.internal.activity.LeakActivity"
            )
            packageManager.setComponentEnabledSetting(
                leakActivity,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}