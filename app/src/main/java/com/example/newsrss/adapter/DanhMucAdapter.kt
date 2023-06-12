package com.example.newsrss.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.newsrss.R
import com.example.newsrss.model.DanhMuc
import com.squareup.picasso.Picasso

class DanhMucAdapter(var context: Context, var layout : Int, var arrDanhMuc: List<DanhMuc>):BaseAdapter() {
    override fun getCount(): Int {
        return arrDanhMuc.size
    }

    override fun getItem(p0: Int): Any {
        return arrDanhMuc[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View? = p1
        if (view == null) {
            var inflater: LayoutInflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.dong_danh_muc, null)
        }
        var dm: DanhMuc = getItem(p0) as DanhMuc
        if (dm != null) {
            var txtTen: TextView = view!!.findViewById(R.id.txtDanhMuc)
            var pic : ImageView = view!!.findViewById(R.id.picDanhMuc)
            txtTen.setText(dm.tenDanhMuc)
            Picasso.with(context).load(dm.thumb).into(pic)
        }
        return view!!
    }

}