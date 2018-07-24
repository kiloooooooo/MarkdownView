package jp.dev.kosuke.markdownview

import android.content.Context
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

class MarkdownView: RelativeLayout {
    var rendererListener: RendererListener? = null

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private var isReady = false
    private var content = ""

    private var showProgressBar = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.MarkdownView)
        showProgressBar = arr.getBoolean(0, true)
        arr.recycle()
        init(context)
    }

    private fun init(context: Context) =
            if (isInEditMode) {
                val text = TextView(context)
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
                addView(text, params)
            }
            else {
                webView = WebView(context)
                progressBar = HorizontalProgressBar(context)

                progressBar.isIndeterminate = true

                val webViewParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                val progressParams = RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

                addView(webView, webViewParams)

                if (showProgressBar)
                    addView(progressBar, progressParams)

                val clientListener = object: WebViewClientListener {
                    override fun onPageStarted() {
                        progressBar.visibility = View.VISIBLE
                        rendererListener?.onRenderStarted()
                    }

                    override fun onPageFinished() {
                        isReady = true
                        render(content)
                    }

                    override fun onReceivedError(error: WebResourceError) {
                        progressBar.visibility = View.GONE
                        isReady = true
                        render("# FAILED!!\n$error")
                        rendererListener?.onError(error)
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

                val rendererCallback = object {
                    @JavascriptInterface
                    fun onRenderFinished() {
                        rendererListener?.onRenderFinished()
                    }
                }

                if (Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT)
                    WebView.enableSlowWholeDocumentDraw()

                webView.settings.javaScriptEnabled = true
                webView.isVerticalScrollBarEnabled = true
                webView.isHorizontalFadingEdgeEnabled = false
                webView.webViewClient = MdWebViewClient(clientListener)
                webView.addJavascriptInterface(progressBarController, "progressBar")
                webView.addJavascriptInterface(rendererCallback, "rendererCallback")
                webView.loadUrl("file:///android_asset/template.html")
            }

    fun render(markdown: String) {
        if (isReady) {
            val replacedMd = markdown.replace("\n", "  \\n")
                                     .replace("\'", "\\\\\'")

            content = replacedMd
            val url = "javascript:loadMarkdown('$content')"
            webView.loadUrl(url)
        }
        else {
            content = markdown
        }
    }

    fun loadCss(css: String) {
        val url = "javascript:loadCss('$css')"
        webView.loadUrl(url)
    }
}
