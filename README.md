# MarkdownView
[![](https://jitpack.io/v/KosukeTakahashi/markdownview.svg)](https://jitpack.io/#KosukeTakahashi/markdownview)

This is MarkdownView for Android.

# Screenshots
<img src="images/1.png" width="200px"></img>
<img src="images/2.png" width="200px"></img>
<img src="images/3.png" width="200px"></img>
<img src="images/4.png" width="200px"></img>

# Usage
1. Add following to root `build.gradle`
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

2. Add the dependency to `build.gradle`
```groovy
...
dependencies {
  implementation 'com.github.KosukeTakahashi:markdownview:v1.3.4'
}
...
```

3. Use
```xml
...
<jp.dev.kosuke.markdownview.MarkdownView
	xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/markdown"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="16dp"
	app:showProgressBar="true"/> <!-- if "false", top ProgressBar will not be shown. -->
...
```

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_main)

  val md = """
      # Foo
      Bar
      **Baz**
      ...
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
  val css = """
      h1 {
        color: blue;
      }
  """.trimIndent()
  
  markdown.rendererListener = listener
  markdown.loadCss(css)
  markdown.render(md)
}
```

# TODO
- [x] ~~Custom CSS~~ **Done!**

# License
See [LICENSE.md](LICENSE.md)

# Libraries
Using following libraries:
- [marked.js](https://github.com/markedjs/marked)
- [highlight.js](https://github.com/isagalaev/highlight.js)
- [MathJax](https://github.com/mathjax/MathJax)
