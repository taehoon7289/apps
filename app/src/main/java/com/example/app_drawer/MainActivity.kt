package com.example.app_drawer

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.app_drawer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding!!.root)

        binding!!.mainText.text = "안녕!!"

        val packages = packageManager.getInstalledPackages(0)
        for (p in packages) {
            p.apply {
                Log.d("package", "packageName $packageName")
                val logoDrawable = packageManager.getApplicationLogo(packageName)
                val iconDrawable = packageManager.getApplicationIcon(packageName)

//                val imageViewForLogo: ImageView = findViewById<ImageView>(R.id.image_logo)
//                imageViewForLogo.setImageDrawable(logoDrawable)
//
//                val imageViewForIcon: ImageView = findViewById<ImageView>(R.id.image_icon)
//                imageViewForIcon.setImageDrawable(iconDrawable)

                binding!!.imageLogo.setImageDrawable(logoDrawable)
                binding!!.imageIcon.setImageDrawable(iconDrawable)



                Log.d("package", "ddd ${packageManager.getApplicationIcon(packageName)}")

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


    }
}