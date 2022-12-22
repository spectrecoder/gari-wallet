package io.coin.gari.ui.auth.webgari

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import io.coin.gari.R
import io.coin.gari.ui.auth.core.AbstractAuthActivity
import io.coin.gari.utils.decodeHex
import org.json.JSONObject


internal class WebGariAuthActivity : AbstractAuthActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_auth)
        val webView = findViewById<WebView>(R.id.webView)

        webView.run {
            /* setBackgroundColor(ContextCompat.getColor(this@WebAuthActivity, R.color.colorGariBackground)) */

            webChromeClient = MultiWindowChromeClient()
            webViewClient = WebViewClient()

            addJavascriptInterface(JsBridge(), "JSBridge")

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        }

        val jwtToken = intent.getStringExtra(ARG_JWT_TOKEN)

        if (jwtToken.isNullOrEmpty()) {
            cancelLoginRequest()
            return
        }

        webView.loadUrl("https://get-wallet-webview.vercel.app?token=${jwtToken}")
    }

    companion object {

        private const val ARG_JWT_TOKEN = "ARG_JWT_TOKEN"

        fun buildIntent(context: Context, jwtToken: String): Intent {
            return Intent(context, WebGariAuthActivity::class.java)
                .also {
                    it.putExtra(ARG_JWT_TOKEN, jwtToken)
                }
        }
    }

    private inner class JsBridge {

        @JavascriptInterface
        fun getKeyPair(jsonArgs: String) {
            val jsonArgsObj = JSONObject(jsonArgs)
            val encodedPrivateKey = jsonArgsObj.optString("privateKey")
            val privateKey = encodedPrivateKey.decodeHex()
            completeLoginSuccess(privateKey)
        }
    }

    private inner class MultiWindowChromeClient : WebChromeClient() {

        var iframeWebView: WebView? = null
        var iframeDialog: AlertDialog? = null

        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message
        ): Boolean {
            iframeWebView = WebView(this@WebGariAuthActivity)
                .apply {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false

                    addJavascriptInterface(JsBridge(), "JSBridge")

                    webViewClient = WebViewClient()
                    webChromeClient = MultiWindowChromeClient()

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true

                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

            iframeDialog = AlertDialog.Builder(this@WebGariAuthActivity)
                .setTitle("")
                .setView(iframeWebView)
                .create()

            iframeDialog?.window?.let {
                val lp = WindowManager.LayoutParams()
                lp.copyFrom(it.attributes)
                lp.width = WindowManager.LayoutParams.MATCH_PARENT
                lp.height = WindowManager.LayoutParams.MATCH_PARENT
                iframeDialog?.show()
                iframeDialog?.window?.attributes = lp

                it.clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                )
            }

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(iframeWebView, true)

            val transport = resultMsg.obj as? WebViewTransport
            transport?.webView = iframeWebView

            resultMsg.sendToTarget()
            return true
        }

        override fun onCloseWindow(window: WebView?) {
            kotlin.runCatching { iframeWebView?.destroy() }
            kotlin.runCatching { iframeDialog?.dismiss() }
        }
    }
}