package org.odddev.fantlab.core.utils

import android.text.Html
import android.text.Spanned

/**
 * @author Ivan Zolotarev
 * @since 30.05.17
 */

@Suppress("DEPRECATION")
fun CharSequence.formatText(): Spanned {
    val baseUrl = "https://fantlab.ru"
    val simpleTag = "b|i|u|s|p"
    val internalTag = "autor|award|work|edition|person|user|art|dictor|series|film|translator"
    val urlTag = "url|URL"
    val html = this
            .replace("\\[($simpleTag)](.*)\\[/\\1]".toRegex(), "<$1>$2</$1>")
            .replace("\\[($internalTag)=(\\d+)](.*)\\[/\\1]".toRegex(), "<a href=\"$baseUrl/$1$2\">$3</a>")
            .replace("\\[pub=(\\d+)](.*)\\[/pub]".toRegex(), "<a href=\"$baseUrl/publisher$1\">$2</a>")
            .replace("\\[link=(.*)](.*)\\[/link]".toRegex(), "<a href=\"$baseUrl/$1\">$2</a>")
            .replace("\\[($urlTag)=(.*)](.*)\\[/\\1]".toRegex(), "<a href=\"$2\">$3</a>")
            .replace("[PHOTO]", "")
            .replace("\r?\n".toRegex(), "<br/>")

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    } else {
        return Html.fromHtml(html)
    }
}