package com.example.app_drawer

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.app_drawer.databinding.ActivityNotionWebViewBinding

class NotionWebViewActivity : AppCompatActivity() {

    private lateinit var activityNotionWebViewBinding: ActivityNotionWebViewBinding
    private lateinit var notionWewView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNotionWebViewBinding = ActivityNotionWebViewBinding.inflate(layoutInflater)
        setContentView(activityNotionWebViewBinding.root)
    }

    override fun onStart() {
        super.onStart()
        notionWewView = activityNotionWebViewBinding.notionWebView
//        notionWewView.loadUrl("https://www.notion.so/4eee128c42924a89a885c1bef88ffacf")
//        notionWewView.settings.loadWithOverviewMode = true
        notionWewView.settings.javaScriptEnabled = true
        notionWewView.settings.domStorageEnabled = true
        notionWewView.webViewClient = WebViewClient()
        notionWewView.loadUrl("https://wandering-haircut-90d.notion.site/4eee128c42924a89a885c1bef88ffacf")
    }
}