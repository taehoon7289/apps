package com.example.app_drawer.ui.notion

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isGone
import com.example.app_drawer.BaseActivity
import com.example.app_drawer.R
import com.example.app_drawer.databinding.ActivityNotionBinding

class NotionActivity : BaseActivity<ActivityNotionBinding>() {

    override val layoutRes: Int = R.layout.activity_notion
    override val backDoubleEnableFlag: Boolean = false

    override fun onStart() {
        super.onStart()
        with(binding) {
            webviewNotion.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    Log.d(TAG, "onProgressChanged: $newProgress")
                    super.onProgressChanged(view, newProgress)
                    if (newProgress > 99) {
                        lottieNotion.isGone = true
                        webviewNotion.isGone = false
                    } else {
                        lottieNotion.isGone = false
                        webviewNotion.isGone = true
                    }
                }
            }
        }
        onSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun onSetting() {

        with(binding.webviewNotion) {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            intent.extras.let {
                val url = it?.getString("url", "")
                if (url?.isEmpty() == false) {
                    loadUrl(url)
                }
            }
        }
    }


    companion object {
        private const val TAG = "NotionActivity"
    }
}

