package ru.fantlab.android.ui.widgets.markdown

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import ru.fantlab.android.ui.widgets.FontEditText
import java.util.*

class MarkdownEditText : FontEditText {

    var savedText: CharSequence = ""
    private var mention: ListView? = null
    private var listDivider: View? = null
    private var inMentionMode = -1
    private var participants: ArrayList<String>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initListView(mention: ListView?, listDivider: View?, participants: ArrayList<String>?) {
        this.mention = mention
        this.listDivider = listDivider
        this.participants = participants
        this.participants?.let {
            mention?.setOnItemClickListener { _, _, position, _ ->
                try {
                    if (inMentionMode != -1) {
                        val complete = mention.adapter.getItem(position).toString() + " "
                        val end = selectionEnd
                        text.replace(inMentionMode, end, complete, 0, complete.length)
                        inMentionMode = -1
                    }
                } catch (ignored: Exception) {
                }
                mention.visibility = GONE
                listDivider?.visibility = GONE
            }
        }
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (isEnabled) {
            savedText = text
        }
    }

    @SuppressLint("SetTextI18n") override fun setText(text: CharSequence, type: TextView.BufferType) {
        try {
            super.setText(text, type)
        } catch (e: Exception) {
            setText("I tried, but your OEM just sucks because they modify the framework components and therefore causing the app to crash!" + "" +
                    ".\nFantLab")
            Crashlytics.logException(e)
        }

    }

    private fun updateMentionList(mentioning: String) {
        participants?.let {
            val mentions = it.filter { it.toLowerCase().startsWith(mentioning.replace("@", "").toLowerCase()) }
            val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                    android.R.id.text1, mentions.subList(0, Math.min(mentions.size, 3)))
            mention?.setAdapter(adapter)
        }
    }

}
