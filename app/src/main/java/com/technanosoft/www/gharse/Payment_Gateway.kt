package com.technanosoft.www.gharse

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.webkit.WebView
import android.webkit.WebViewClient
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.ImageButton
import android.widget.LinearLayout


class Payment_Gateway : AppCompatActivity() {

    private var payment:WebView? = null
    val bundle = Bundle()


    private var mMainWebView: WebView? = null
    private var mSubWebView: WebView? = null
    private var mSubViewGroup: LinearLayout? = null
    private var mSubCloseBtn: ImageButton? = null

    private val mWebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            if(url!!.contains("Response")){
                mWebChromeClient.onCloseWindow(mSubWebView)
                val intent = Intent(this@Payment_Gateway,User_Home::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private val mWebChromeClient = object : WebChromeClient() {



        override fun onCreateWindow(view: WebView, dialog: Boolean, userGesture: Boolean, resultMsg: Message): Boolean {
            if (null == mSubWebView) {

                val subWebView = WebView(this@Payment_Gateway)
                subWebView.setWebViewClient(WebViewClient())
                subWebView.setWebChromeClient(WebChromeClient())
                subWebView.settings.javaScriptEnabled = true
                subWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                subWebView.settings.setSupportMultipleWindows(true)
                mSubWebView = subWebView

                mSubViewGroup!!.addView(subWebView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT))
                mSubViewGroup!!.visibility = View.VISIBLE
                subWebView.requestFocus()

                val transport = resultMsg.obj as WebView.WebViewTransport
                transport.webView = mSubWebView
                resultMsg.sendToTarget()
                return true
            }
            return false
        }

        override fun onCloseWindow(window: WebView?) {
            mSubViewGroup!!.visibility = View.GONE
            mSubViewGroup!!.removeView(mSubWebView)
            mSubWebView?.onPause()
            mSubWebView = null
            mMainWebView!!.requestFocus()
            super.onCloseWindow(window)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment__gateway)
        val sh = SharedPrefManager()
        sh.SharedPrefManager_fun(this)
        val user = sh.getUser()
        var finame = user.Name.toString()
        val name = finame.split(" ")
        finame = name[0]
        val txnid = "txnid="+intent.getStringExtra("tx")
        val amount = "&amount="+intent.getStringExtra("am")
        val pinfo = "&pinfo="+intent.getStringExtra("pin")
        val fname = "&fname="+finame
        val email = "&email="+user.Email.toString()
        val mobile = "&mobile="+user.Mobile.toString()

        val url = BuildConfig.Pay_Start+txnid+amount+pinfo+fname+email+mobile+BuildConfig.Pay_End


        mMainWebView = findViewById(R.id.main_webview) as WebView?
        mMainWebView!!.setWebViewClient(mWebViewClient)
        mMainWebView!!.setWebChromeClient(mWebChromeClient)
        mMainWebView!!.settings.javaScriptEnabled = true
        mMainWebView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        mMainWebView!!.settings.setSupportMultipleWindows(true)
        mMainWebView!!.loadUrl(url)

        mSubViewGroup = findViewById(R.id.sub_group) as LinearLayout?

        mSubCloseBtn = mSubViewGroup!!.findViewById(R.id.sub_close_btn) as ImageButton?
        mSubCloseBtn!!.setOnClickListener({ v -> mWebChromeClient.onCloseWindow(mSubWebView)} )
    }

    override fun onStart() {
        super.onStart()
        mMainWebView!!.onResume()
        mMainWebView!!.requestFocus()
        mSubWebView?.onResume()
        mSubWebView?.requestFocus()
    }

    override fun onStop() {
        mMainWebView!!.onPause()
        mSubWebView?.onPause()
        super.onStop()
    }

    override fun onBackPressed() {
        if (null != mSubWebView) {
            if (mSubWebView!!.canGoBack()) {
                mSubWebView!!.goBack()
                return
            } else {
                mSubCloseBtn!!.performClick()
                return
            }
        } else if (mMainWebView!!.canGoBack()) {
            mMainWebView!!.goBack()
            return
        }
        super.onBackPressed()
    }
}
