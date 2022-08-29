package com.example.app_drawer

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val packages = packageManager.getInstalledPackages(0)
        for (p in packages) {
            p.apply {
                Log.d("package", "packageName $packageName")
                Log.d("package", "versionName $versionName")
                Log.d("package", "lastUpdateTime $lastUpdateTime")
                applicationInfo.apply {
                    Log.d("package applicationInfo", "firstInstallTime :: $firstInstallTime")
                    Log.d("package applicationInfo", "targetSdkVersion :: $targetSdkVersion")
                    Log.d("package applicationInfo", "minSdkVersion :: $minSdkVersion")
                    Log.d("package applicationInfo", "sourceDir :: $sourceDir")
                    Log.d("package applicationInfo", "uid :: $uid")
                    Log.d("package applicationInfo", "label :: ${loadLabel(packageManager)}")
                    Log.d("package applicationInfo", "processName :: $processName")
                    Log.d("package applicationInfo", "publicSourceDir :: $publicSourceDir")
                }
            }
        }

        setContentView(R.layout.activity_main)
    }
}