package com.example.newsrss.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsrss.R
import com.example.newsrss.XMLDOMParser
import com.example.newsrss.adapter.DocBaoAdapter
import com.example.newsrss.model.DanhMuc
import com.example.newsrss.model.DocBao
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    lateinit var lvTieude : ListView
    lateinit var arrayLink : ArrayList<String>
    lateinit var arrayDocBao: ArrayList<DocBao>
    lateinit var adapter: DocBaoAdapter
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var txtTenDM = findViewById<TextView>(R.id.txtTenDanhMuc)
        lvTieude = findViewById(R.id.lvTieuDe)
        toolbar = findViewById(R.id.toolbarMain)
        actionToolbar()


        arrayLink = ArrayList()
        arrayDocBao = ArrayList()
        adapter= DocBaoAdapter(applicationContext,android.R.layout.simple_list_item_1,arrayDocBao)
        lvTieude.setAdapter(adapter)
        txtTenDM.setText("Thế giới")
        var link = "https://vnexpress.net/rss/the-gioi.rss"

        var danhMuc : DanhMuc? = intent.getSerializableExtra("danhmuc") as DanhMuc?

        if(danhMuc !=null){
            txtTenDM.setText(danhMuc.tenDanhMuc)
            link = danhMuc.linkDM
        }

        ReadRSS().execute(link)

        lvTieude.setOnItemClickListener { adapterView, view, i, l ->
            var intent = Intent(this, NewActivity::class.java)
            intent.putExtra("linkTinTuc",arrayLink.get(i))
            startActivity(intent)
        }



    }

    private fun actionToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    inner class ReadRSS : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {

            var content : StringBuilder = StringBuilder()
            var url : URL = URL(p0[0])
            val inputStreamReader = InputStreamReader(url.openConnection().getInputStream())
            val bufferedReader  = BufferedReader(inputStreamReader)

            var line = ""
            try {
                do {
                    line = bufferedReader.readLine()
                    if(line!=null){
                        content.append(line)
                    }
                }while (line != null)
                bufferedReader.close()
            }
            catch (e : java.lang.Exception){
                Log.d("AAA",e.toString())
            }
            return content.toString()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var parser = XMLDOMParser()
            var document : Document? = parser.getDocument(result)
            var nodeList : NodeList = document!!.getElementsByTagName("item")
            var nodeListDesc : NodeList = document.getElementsByTagName("description")
            var tieuDe  = ""
            var hinhAnh = ""
            var link = ""
            var date = ""
            var des = ""
            for( i in 0..(nodeList.length-3)){

                var cdata : String = nodeListDesc.item(i+1).textContent
                val p: Pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>")
                val matcher : Matcher = p.matcher(cdata)
                if(matcher.find()){
                    hinhAnh = matcher.group(1)

                }
                var element : Element = nodeList.item(i) as Element
                tieuDe = parser.getValue(element,"title")
                link = parser.getValue(element,"link")
                date = parser.getValue(element,"pubDate")
                des = parser.getValue(element,"description")
                arrayDocBao.add(DocBao(tieuDe, link,hinhAnh,date,des))
                arrayLink.add(parser.getValue(element,"link"))
            }

            adapter.notifyDataSetChanged()

        }

    }


}