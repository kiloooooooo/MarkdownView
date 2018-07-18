package jp.dev.kosuke.markdownview

import android.webkit.WebResourceError

internal interface WebViewClientListener {
    fun onPageStarted()
    fun onPageFinished()
    fun onReceivedError(error: WebResourceError)
}
