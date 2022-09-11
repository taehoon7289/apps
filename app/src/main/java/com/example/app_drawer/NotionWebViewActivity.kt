package com.example.app_drawer

import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isGone
import com.example.app_drawer.databinding.ActivityNotionWebViewBinding

class NotionWebViewActivity :
    BindActivity<ActivityNotionWebViewBinding>(R.layout.activity_notion_web_view) {

    private lateinit var notionWewView: WebView
    private val TAG = "NotionWebViewActivity"

    override fun onStart() {
        super.onStart()
        notionWewView = binding.notionWebView
        notionWewView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                Log.d(TAG, "onProgressChanged: $newProgress")
                super.onProgressChanged(view, newProgress)
                if (newProgress > 90) {
                    binding.notionWebViewAnimationView.isGone = true
                    binding.notionWebView.isGone = false
                } else {
                    binding.notionWebViewAnimationView.isGone = false
                    binding.notionWebView.isGone = true
                }
            }
        }
//        notionWewView.loadUrl("https://www.notion.so/4eee128c42924a89a885c1bef88ffacf")
//        notionWewView.settings.loadWithOverviewMode = true
        notionWewView.settings.javaScriptEnabled = true
        notionWewView.settings.domStorageEnabled = true
        notionWewView.webViewClient = WebViewClient()
        notionWewView.loadUrl("https://wandering-haircut-90d.notion.site/4eee128c42924a89a885c1bef88ffacf")
    }
}