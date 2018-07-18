package jp.dev.kosuke.markdownview

import android.webkit.WebResourceError

interface RendererListener {
    fun onRenderStarted()
    fun onRenderFinished()
    fun onError(error: WebResourceError)
}
