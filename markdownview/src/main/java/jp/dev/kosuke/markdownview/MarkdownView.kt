package jp.dev.kosuke.markdownview

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.RelativeLayout

class MarkdownView: RelativeLayout {

    interface MarkdownRendererListener {
        fun onRenderStart()
        fun onRenderFinish()
        fun onError(error: WebResourceError)
    }

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private var isReady = false
    private var content = ""

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) =
            if (isInEditMode) {

            }
            else {
                webView = WebView(context)
                progressBar = HorizontalProgressBar(context)

                progressBar.isIndeterminate = true

                val webViewParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                val progressParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

                addView(webView, webViewParams)
                addView(progressBar, progressParams)

                val listener = object: MarkdownRendererListener {

                    override fun onRenderStart() {
                        progressBar.visibility = View.VISIBLE
                    }

                    override fun onRenderFinish() {
                        isReady = true
                        render(content)
                    }

                    override fun onError(error: WebResourceError) {
                        progressBar.visibility = View.GONE
                        isReady = true
                        render("# FAILED!!\n$error")
                    }
                }

                val handler = Handler()
                val progressBarController = object {
                    @JavascriptInterface
                    fun show() {
                        handler.post {
                            progressBar.visibility = View.VISIBLE
                        }
                    }

                    @JavascriptInterface
                    fun hide() {
                        handler.post {
                            progressBar.visibility = View.GONE
                        }
                    }
                }

                val logger = object {
                    @JavascriptInterface
                    fun log(msg: String) {
                        Log.d("MarkdownView Log", msg)
                    }
                }


                if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT)
                    WebView.enableSlowWholeDocumentDraw()

                webView.settings.javaScriptEnabled = true
                webView.isVerticalScrollBarEnabled = true
                webView.isHorizontalFadingEdgeEnabled = false
                webView.webViewClient = MdWebViewClient(listener)
                webView.addJavascriptInterface(progressBarController, "progressBar")
                webView.addJavascriptInterface(logger, "nativeLogger")
                webView.loadUrl("file:///android_asset/template.html")
            }

    fun render(markdown: String) {
        if (isReady) {
            val replacedMd = markdown.replace("\n", "  \\n")
                    .replace("\'", "\\\\\'")
                    .replace("\"", "\\\\\"")

            content = replacedMd
            val url = "javascript:loadMarkdown('$content')"
            Log.d("markdownView", "url => $url")
            webView.loadUrl(url)
        }
        else {
            content = markdown
        }
    }

    inner class MdWebViewClient(private val listener: MarkdownRendererListener): WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            listener.onRenderStart()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            listener.onRenderFinish()
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            listener.onError(error!!)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return true
        }
    }
}
