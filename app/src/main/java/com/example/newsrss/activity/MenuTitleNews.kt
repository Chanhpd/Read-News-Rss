package com.example.newsrss.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsrss.R
import com.example.newsrss.adapter.DanhMucAdapter
import com.example.newsrss.model.DanhMuc

class MenuTitleNews : AppCompatActivity() {
    lateinit var arrayDanhMuc : ArrayList<DanhMuc>
    lateinit var adapter : DanhMucAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_title_news)

        val lvDanhMuc : ListView = findViewById(R.id.listviewDanhMuc)
        arrayDanhMuc = ArrayList()
//        addDanhMuc()
        adapter = DanhMucAdapter(applicationContext,android.R.layout.simple_list_item_1,arrayDanhMuc)
        lvDanhMuc.adapter = adapter
        lvDanhMuc.setOnItemClickListener { adapterView, view, i, l ->

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("danhmuc", arrayDanhMuc[i])
            intent.putExtra("linkDM",arrayDanhMuc.get(i).linkDM)
            startActivity(intent)
        }
    }
//    fun addDanhMuc(){
//        arrayDanhMuc.add(DanhMuc("Thế Giới","https://vnexpress.net/rss/the-gioi.rss"))
//        arrayDanhMuc.add(DanhMuc("Thời sự","https://vnexpress.net/rss/thoi-su.rss"))
//        arrayDanhMuc.add(DanhMuc("Cười","https://vnexpress.net/rss/cuoi.rss"))
//        arrayDanhMuc.add(DanhMuc("Sức khỏe","https://vnexpress.net/rss/suc-khoe.rss"))
//        arrayDanhMuc.add(DanhMuc("Đời sống","https://vnexpress.net/rss/gia-dinh.rss"))
//        arrayDanhMuc.add(DanhMuc("Tin nổi bật","https://vnexpress.net/rss/tin-noi-bat.rss"))
//        arrayDanhMuc.add(DanhMuc("Tin xem nhiều","https://vnexpress.net/rss/tin-xem-nhieu.rss"))
//        arrayDanhMuc.add(DanhMuc("Tin mới nhất","https://vnexpress.net/rss/tin-moi-nhat.rss"))
//        arrayDanhMuc.add(DanhMuc("Thể thao","https://vnexpress.net/rss/the-thao.rss"))
//        arrayDanhMuc.add(DanhMuc("Giáo dục","https://vnexpress.net/rss/giao-duc.rss"))
//        arrayDanhMuc.add(DanhMuc("Kinh doanh","https://vnexpress.net/rss/kinh-doanh.rss"))
//        arrayDanhMuc.add(DanhMuc("Du lịch","https://vnexpress.net/rss/du-lich.rss"))
//        arrayDanhMuc.add(DanhMuc("Xe","https://vnexpress.net/rss/oto-xe-may.rss"))
//        arrayDanhMuc.add(DanhMuc("Tâm sự","https://vnexpress.net/rss/tam-su.rss"))
//    }
}