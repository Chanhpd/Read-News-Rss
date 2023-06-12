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
import com.example.newsrss.activity.NewActivity
import com.example.newsrss.model.DocBao
import com.squareup.picasso.Picasso

class PhotoAdapter(var context: Context,var mListPhoto : List<DocBao>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_photo)
        var title : TextView = itemView.findViewById(R.id.tv_title_banner)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        var view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_photo,parent,false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        var photo : DocBao = mListPhoto.get(position)
        if(photo ==null){
            return
        }

        Picasso.with(context).load(photo.image).into(holder.imgPhoto )
        holder.title.text = photo.title
        holder.itemView.setOnClickListener {
            var intent = Intent(context, NewActivity::class.java)
//            intent.putExtra("danhmuc", photo)
            intent.putExtra("linkTinTuc",photo.link)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if(mListPhoto != null){
            return mListPhoto.size
        }
        return 0
    }
}