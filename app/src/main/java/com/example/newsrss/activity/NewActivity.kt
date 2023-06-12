package com.example.newsrss.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.widget.Toolbar
import com.example.newsrss.R

class NewActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        var intent : Intent = getIntent()
        var link:String = intent.getStringExtra("linkTinTuc") as String
        var webView : WebView = findViewById(R.id.webView)
        webView.loadUrl(link)
        // chỉ chạy trong app
        webView.webViewClient= WebViewClient()

        actionToolbar()
    }
    private fun actionToolbar() {
        toolbar = findViewById(R.id.toolbar_news)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}