function loadMarkdown(content) {
    progressBar.show()

    let callback = (err, out) => {
        progressBar.hide()
        rendererCallback.onRenderFinished()

        if (err)
            return err
        else
            return out
    }

    let markdownContent = document.querySelector('#markdown_content')

    let parsed = content.replace("\\(", "<intex>")
                        .replace("\\)", "</intex>")
                        .replace("\\[", "<dtex>")
                        .replace("\\]", "</dtex>")

    let html = marked(parsed, null, callback)

    let parsedHtml = html.replace("<intex>", "\\(")
                         .replace("</intex>", "\\)")
                         .replace("<dtex>", "\\[")
                         .replace("</dtex>", "\\]")

    markdownContent.innerHTML = parsedHtml

    console.log("markdown parsed =>\n" + parsedHtml)

    MathJax.Hub.Typeset(markdownContent, () => {})
    let elems = document.querySelectorAll('code')
    for (elem of elems) {
        hljs.highlightBlock(elem)
    }
}

function loadCss(css) {
    let cssElem = document.createElement('style')
    cssElem.type = 'text/css'
    cssElem.innerHTML = css
    document.head.appendChild(cssElem)
}
