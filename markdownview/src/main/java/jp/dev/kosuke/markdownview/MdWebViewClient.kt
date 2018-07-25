package jp.dev.kosuke.markdownview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

internal class MdWebViewClient(private val listener: WebViewClientListener): WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        listener.onPageStarted()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        listener.onPageFinished()
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        listener.onReceivedError(error!!)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean =
            if (view != null && request != null) {
                val intent = Intent(Intent.ACTION_VIEW, request.url)

                view.context.startActivity(intent)

                true
            }
            else {
                false
            }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean =
            if (view != null && url != null) {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                view.context.startActivity(intent)

                true
            }
            else {
                false
            }
}
