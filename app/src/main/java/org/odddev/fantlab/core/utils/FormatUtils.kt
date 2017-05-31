package org.odddev.fantlab.core.utils

import android.text.Html
import android.text.Spanned
import java.util.*

/**
 * @author Ivan Zolotarev
 * @since 30.05.17
 */

@Suppress("DEPRECATION")
fun CharSequence.formatText(): Spanned {
    val baseUrl = "https://fantlab.ru"
    //val simpleTag = "b|i|u|s|p"
    val internalTag = "autor|award|work|edition|person|user|art|dictor|series|film|translator"
    val urlTag = "url|URL"
    val html = this
            // простые теги могут быть вложенными, так что обращение \\1 не сработает для вложенных тегов
            // как обойти покороче, пока не придумал
            //.replace("\\[($simpleTag)](.*?)\\[/\\1]".toRegex(), "<$1>$2</$1>")
            .replace("\\[b](.*?)\\[/b]".toRegex(), "<b>$1</b>")
            .replace("\\[i](.*?)\\[/i]".toRegex(), "<i>$1</i>")
            .replace("\\[u](.*?)\\[/u]".toRegex(), "<u>$1</u>")
            .replace("\\[s](.*?)\\[/s]".toRegex(), "<s>$1</s>")
            .replace("\\[p](.*?)\\[/p]".toRegex(), "<p>$1</p>")
            //.replace("\\[q=?(.*?)](.*?)[/q]".toRegex(), "<blockquote><p>\"цитата \"<cite>$1</cite></p><div>$2</div></blockquote>")
            //.replace("\\[h](.*?)[/h]".toRegex(), "<div><p>\"скрытый текст \"<span>(кликните по нему, чтобы увидеть)</span></p><div>$1</div></div>")
            .replace("\\[($internalTag)=(\\d+)](.*?)\\[/\\1]".toRegex(), "<a href=\"$baseUrl/$1$2\">$3</a>")
            .replace("\\[pub=(\\d+)](.*?)\\[/pub]".toRegex(), "<a href=\"$baseUrl/publisher$1\">$2</a>")
            .replace("\\[link=(.*)](.*?)\\[/link]".toRegex(), "<a href=\"$baseUrl/$1\">$2</a>")
            .replace("\\[($urlTag)=(.*)](.*?)\\[/\\1]".toRegex(), "<a href=\"$2\">$3</a>")
            .replace("\\[IMG](.*?)\\[/IMG]".toRegex(), "<img src=\"$1\">")
            .replace("\\[right](.*?)\\[/right]".toRegex(), "<br/>$1")
            .replace("[br]", "<br/>")
            .replace("[PHOTO]", "")
            .replace("\r?\n".toRegex(), "<br/>")

    //todo загрузка img
    //todo нормальная обработка тегов [q],[h], обработка [LIST], [VIDEO] (see https://stackoverflow.com/a/42370792)
    //todo smiles :)

    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
        Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
    else
        Html.fromHtml(html)
}

fun Calendar.format(): String {
    val day = this.get(Calendar.DAY_OF_MONTH)
    val month = this.get(Calendar.MONTH)
    val year = this.get(Calendar.YEAR)
    return "$day.$month.$year"
}