package com.example.newsrss.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsrss.R
import com.example.newsrss.activity.MainActivity
import com.example.newsrss.model.DanhMuc
import com.squareup.picasso.Picasso

class DanhMucTinAdapter(var context: Context, var list: List<DanhMuc>) :
    RecyclerView.Adapter<DanhMucTinAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title: TextView = itemView.findViewById(R.id.tv_tendanhmuc)
        var pic : ImageView = itemView.findViewById(R.id.img_cate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_danh_muc_tin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = list.get(position).tenDanhMuc
        Picasso.with(context).load(list.get(position).thumb).into(holder.pic)
        holder.itemView.setOnClickListener {
            val i = Intent(context,MainActivity::class.java)
            i.putExtra("danhmuc", list.get(position))
            i.putExtra("linkDM",list.get(position).linkDM)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}