package it.estof.staffapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {
 private lateinit var webView: WebView
 private lateinit var swipeRefresh: SwipeRefreshLayout
 private lateinit var progressBar: ProgressBar

 @SuppressLint("SetJavaScriptEnabled")
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  webView = findViewById(R.id.webView)
  swipeRefresh = findViewById(R.id.swipeRefresh)
  progressBar = findViewById(R.id.progressBar)

  swipeRefresh.setOnRefreshListener { webView.reload() }

  webView.settings.apply {
   javaScriptEnabled = true
   domStorageEnabled = true
   cacheMode = WebSettings.LOAD_DEFAULT
   loadWithOverviewMode = true
   useWideViewPort = true
   builtInZoomControls = false
   displayZoomControls = false
  }

  webView.webChromeClient = WebChromeClient()
  webView.webViewClient = object : WebViewClient() {
   override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean = false
   override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
    progressBar.visibility = View.VISIBLE
   }
   override fun onPageFinished(view: WebView?, url: String?) {
    progressBar.visibility = View.GONE
    swipeRefresh.isRefreshing = false
   }
  }

  if (savedInstanceState != null) webView.restoreState(savedInstanceState)
  else webView.loadUrl("https://estof.it/staff-app/")

  onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
   override fun handleOnBackPressed() {
    if (webView.canGoBack()) webView.goBack() else finish()
   }
  })
 }

 override fun onSaveInstanceState(outState: Bundle) {
  webView.saveState(outState)
  super.onSaveInstanceState(outState)
 }
}
