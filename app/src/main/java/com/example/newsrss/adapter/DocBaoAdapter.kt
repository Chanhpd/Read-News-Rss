package com.example.newsrss.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.newsrss.R
import com.example.newsrss.model.DocBao
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class DocBaoAdapter(var context: Context, var layout: Int, var items: List<DocBao>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View? = p1
        if (view == null) {
            var inflater: LayoutInflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.dong_layout_listview, null)
        }
        val db: DocBao = getItem(p0) as DocBao
        val txtTitle: TextView = view!!.findViewById(R.id.txtTitle)

        txtTitle.setText(db.title.limitWordCount(18))
        val img: ImageView = view.findViewById(R.id.imageView)

        Picasso.with(context).load(db.image).into(img)

        val date: TextView = view.findViewById(R.id.tv_pubDate)
        date.text = formatDateTime(db.date)

        val des : TextView = view.findViewById(R.id.tv_description)
        des.text= db.des.limitTextLength(10)

        val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.scale_list)
        view.startAnimation(animation)
        return view
    }

    fun String.limitTextLength(maxLength: Int): String {
        return if (this.length > maxLength) {
            this.substring(0, maxLength) + "..."
        } else {
            this
        }
    }
    fun String.limitWordCount(maxWords: Int): String {
        val words = this.trim().split("\\s+".toRegex())
        return if (words.size > maxWords) {
            words.subList(0, maxWords).joinToString(" ") + "..."
        } else {
            this
        }
    }
    fun formatDateTime(inputDateTime: String): String {
        val inputFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("HH'h'mm,' ngày' dd/MM/yy", Locale.ENGLISH)

        val date: Date = inputFormat.parse(inputDateTime) ?: return ""
        return outputFormat.format(date)
    }
}