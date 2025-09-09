
package com.aiexplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : ComponentActivity() {
    private lateinit var webView: WebView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var progress: android.widget.ProgressBar

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        swipe = findViewById(R.id.swipeRefresh)
        progress = findViewById(R.id.progressBar)

        val s: WebSettings = webView.settings
        s.javaScriptEnabled = true
        s.domStorageEnabled = true
        s.loadsImagesAutomatically = true
        s.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        webView.isVerticalScrollBarEnabled = true
        webView.isHorizontalScrollBarEnabled = false

        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.webChromeClient = object: WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.visibility = View.VISIBLE
                progress.progress = newProgress
                if (newProgress >= 100) progress.visibility = View.GONE
            }
        }

        webView.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url?.toString() ?: return false
                if (url.contains("accounts.google.com") || url.startsWith("mailto:") || url.startsWith("tel:")) {
                    openCustomTab(url)
                    return true
                }
                return false
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) { swipe.isRefreshing = true }
            override fun onPageFinished(view: WebView?, url: String?) { swipe.isRefreshing = false }
        }

        swipe.setOnRefreshListener { webView.reload() }

        intent?.data?.let { _ -> webView.loadUrl(BuildConfig.APP_URL) } ?: run { webView.loadUrl(BuildConfig.APP_URL) }
    }

    private fun openCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle deep link callback if you implement token handoff
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }
}
