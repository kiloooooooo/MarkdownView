package jp.dev.kosuke.markdownview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebResourceError
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val markdown = """
            # Header 1
            ## Header 2
            ### Header 3
            #### Header 4
            ##### Header 5
            ###### Header 6

            # Samples

            **BOLD**
            _Italic_

            ## Code
            ```kotlin:kotlin
            fun main(args: Array<String>) {
                println(\"Hello, Kotlin!\")
            }
            ```

            ## Lists
            ### Normal list
            - Item 1
            - Item 2
            - Item 3

            ### Numbered list
            1. Numbered item 1
            1. Numbered item 2
            1. Numbered item 3

            ## Table
            |Left|Center|Right|
            |:---|:----:|----:|
            |L   |C     |R    |
            |Foo |Bar   |Baz  |

            ## Formula
            ### Inline
            \\( e^{i \\theta} = \\cos \\theta + i \\sin \\theta \\) is known as Euler\'s formula.
            ### Displayed
            \\[
                \\sin (\\alpha + \\beta) = \\sin \\alpha \\cos \\beta + \\cos \\alpha \\sin \\beta
            \\]

            ## Images
            URL: https://commons.wikimedia.org/wiki/File:A300_Iran_Air_EP-IBT_THR_May_2010.jpg?uselang=ja
            ![img](https://upload.wikimedia.org/wikipedia/commons/9/9a/A300_Iran_Air_EP-IBT_THR_May_2010.jpg)

            URL: https://upload.wikimedia.org/wikipedia/commons/b/b5/Kotlin-logo.png
            ![img](https://upload.wikimedia.org/wikipedia/commons/b/b5/Kotlin-logo.png)
        """.trimIndent()

        val listener = object: RendererListener {
            override fun onRenderStarted() {
                Toast.makeText(this@MainActivity, "Started rendering", Toast.LENGTH_SHORT).show()
            }

            override fun onRenderFinished() {
                Toast.makeText(this@MainActivity, "Finished rendering", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: WebResourceError) {
                Toast.makeText(this@MainActivity, "Error occurred! =>\n$error", Toast.LENGTH_LONG).show()
            }
        }

        markdown_test.rendererListener = listener
        markdown_test.render(markdown)

        fab.setOnClickListener { _ ->
            markdown_test.render(markdown)
        }
    }
}
