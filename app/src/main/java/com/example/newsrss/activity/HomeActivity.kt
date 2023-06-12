package com.example.newsrss.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.newsrss.R
import com.example.newsrss.XMLDOMParser
import com.example.newsrss.adapter.DanhMucAdapter
import com.example.newsrss.adapter.DanhMucTinAdapter
import com.example.newsrss.adapter.PhotoAdapter
import com.example.newsrss.model.DanhMuc
import com.example.newsrss.model.DocBao
import com.google.android.material.navigation.NavigationView
import me.relex.circleindicator.CircleIndicator3
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    lateinit var listViewDanhMuc: ListView

    lateinit var cateAdapter: DanhMucAdapter
    lateinit var listCate: ArrayList<DanhMuc>


    lateinit var arrayLink: ArrayList<String>
    lateinit var adapter: PhotoAdapter
    lateinit var listBanner: ArrayList<DocBao>

    // danh muc  tin
    lateinit var recycleViewDanhMuc : RecyclerView
    lateinit var adapterDanhMuc : DanhMucTinAdapter




    private lateinit var mViewPager2: ViewPager2
    private lateinit var mCircleIndicator3: CircleIndicator3
    private lateinit var mListPhoto: List<DocBao>
    private var mHandler = Handler(Looper.getMainLooper())
    private var mRunnable = Runnable {
        var currentPosition: Int = mViewPager2.currentItem
        if (currentPosition == mListPhoto.size - 1) {
            mViewPager2.currentItem = 0
        } else {
            mViewPager2.currentItem = currentPosition + 1
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        showDanhMucTin()
        actionToolbar()
        danhMucTinTuc()
        ReadRSS().execute("https://vnexpress.net/rss/tin-noi-bat.rss")

        viewPager()
        showDanhMucTin()
    }

    private fun showDanhMucTin() {
        addDanhMuc()

        recycleViewDanhMuc.setHasFixedSize(true)
        recycleViewDanhMuc.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycleViewDanhMuc.adapter = adapterDanhMuc
    }

    inner class ReadRSS : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg p0: String?): String {
            val content: StringBuilder = StringBuilder()
            val url: URL = URL(p0[0])
            val inputStreamReader = InputStreamReader(url.openConnection().getInputStream())
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String? = ""
            try {
                do {
                    line = bufferedReader.readLine()
                    if (line != null) {
                        content.append(line)
                    }
                } while (line != null)
                bufferedReader.close()
            } catch (e: java.lang.Exception) {
                Log.d("AAA", e.toString())
            }
            return content.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parser = XMLDOMParser()
            val document: Document? = parser.getDocument(result)
            val nodeList: NodeList = document!!.getElementsByTagName("item")
            val nodeListDesc: NodeList = document.getElementsByTagName("description")
            var tieuDe = ""
            var hinhAnh = ""
            var link = ""
            var date = ""
            var des = ""
            for (i in 0 until nodeList.length - 3) {
                val cdata: String = nodeListDesc.item(i + 1).textContent
                val p: Pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>")
                val matcher: Matcher = p.matcher(cdata)
                if (matcher.find()) {
                    hinhAnh = matcher.group(1)
                }
                val element: Element = nodeList.item(i) as Element
                tieuDe = parser.getValue(element, "title")
                link = parser.getValue(element, "link")
                date = parser.getValue(element,"pubDate")
                des = parser.getValue(element,"description")
                listBanner.add(DocBao(tieuDe, link, hinhAnh,date,des))
                arrayLink.add(parser.getValue(element, "link"))

            }

            adapter.notifyDataSetChanged()
        }

    }

    private fun viewPager() {
        mViewPager2 = findViewById(R.id.view_pager_2)
        mCircleIndicator3 = findViewById(R.id.circle_indicator_3)

        // setting viewpager
        mViewPager2.offscreenPageLimit = 3
        mViewPager2.clipToPadding = false
        mViewPager2.clipChildren = false


        //transformer
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            var r: Float = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f

        })
        mViewPager2.setPageTransformer(compositePageTransformer)

        mListPhoto = ArrayList()

        mViewPager2.adapter = adapter
        mCircleIndicator3.setViewPager(mViewPager2)


        mViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mHandler.removeCallbacks(mRunnable)
                mHandler.postDelayed(mRunnable, 4000)
            }
        })
    }

    private fun danhMucTinTuc() {
        listCate = ArrayList()

        cateAdapter = DanhMucAdapter(this, R.layout.dong_danh_muc, listCate)
        listViewDanhMuc.adapter = cateAdapter
        
        listViewDanhMuc.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("danhmuc", listCate[position])
            intent.putExtra("linkDM",listCate.get(position).linkDM)
            startActivity(intent)
        }
    }

    private fun actionToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.ic_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun initView() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)
        listViewDanhMuc = findViewById(R.id.lvDanhMuc)
        listBanner = ArrayList()
        arrayLink = ArrayList()
        adapter = PhotoAdapter(this, listBanner)
        listCate = ArrayList()

        recycleViewDanhMuc = findViewById(R.id.recycleView_danhmuctin)

        adapterDanhMuc = DanhMucTinAdapter(this,listCate)
    }

    fun addDanhMuc() {
        listCate.add(DanhMuc("Thế Giới", "https://vnexpress.net/rss/the-gioi.rss","https://www.vhv.rs/dpng/d/600-6006382_news-transparent-globe-news-earth-logo-png-png.png"))
        listCate.add(DanhMuc("Thời sự", "https://vnexpress.net/rss/thoi-su.rss","https://e7.pngegg.com/pngimages/4/734/png-clipart-globe-gnome-miscellaneous-cdr.png"))

        listCate.add(DanhMuc("Pháp luật","https://vnexpress.net/rss/phap-luat.rss","https://baohaiquanvietnam.vn/storage/users/user_6/nawm%202020/thang%201-2020/15-1/c40.jpg"))
        listCate.add(DanhMuc("Startup","https://vnexpress.net/rss/startup.rss","https://imgs.vietnamnet.vn/Images/2016/09/01/08/20160901084057-startup.jpg"))
        listCate.add(DanhMuc("Khoa học","https://vnexpress.net/rss/khoa-hoc.rss","https://r73troypb4obj.vcdn.cloud/website02/uploads/images/6242dc401a1b854d4e9030b8/tu-duy-khoa-hoc-la-gi-va-tim-hieu-cac-phuong-phap-hinh-thanh-tu-duy-khoa-hoc.jpg"))
        listCate.add(DanhMuc("Ý kiến","https://vnexpress.net/rss/y-kien.rss","https://www.studytienganh.vn/upload/2021/05/98728.png"))

        listCate.add(DanhMuc("Giải trí","https://vnexpress.net/rss/y-kien.rss","https://yt3.googleusercontent.com/ytc/AGIKgqPJBmFgtYO9lpS9adTgvy6MK_y-Bf1QLYe3q0f-FA=s900-c-k-c0x00ffffff-no-rj"))
        listCate.add(DanhMuc("Du lịch","https://vnexpress.net/rss/du-lich.rss","https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2022/7/10/hinh-anh-cac-loai-hinh-du-lich-3-1657423025597-1657423027180128362217.jpeg"))
        listCate.add(DanhMuc("Số hóa","https://vnexpress.net/rss/so-hoa.rss","https://tino.org/wp-content/uploads/2021/08/word-image-123.jpeg"))
        listCate.add(DanhMuc("Cười", "https://vnexpress.net/rss/cuoi.rss","https://i0.wp.com/thatnhucuocsong.com.vn/wp-content/uploads/2022/02/anh-mat-cuoi-tit-mat-1.png?ssl\\u003d1"))
        listCate.add(DanhMuc("Sức khỏe", "https://vnexpress.net/rss/suc-khoe.rss","https://yt3.googleusercontent.com/ytc/AGIKgqMr39Fo7LMI1oi6ALBoDoqIM4kbsAE6-ek_I7Tb=s900-c-k-c0x00ffffff-no-rj"))
        listCate.add(DanhMuc("Đời sống", "https://vnexpress.net/rss/gia-dinh.rss","https://lamnguoi.net/Uploads/images/NgheThuatSong/2020/Lam-sao-de-gia-dinh-luon-hanh-phuc-lamnguoinet.jpg"))
        listCate.add(DanhMuc("Tin nổi bật", "https://vnexpress.net/rss/tin-noi-bat.rss","https://www.clipartmax.com/png/middle/232-2325125_%E0%B8%9B%E0%B9%89%E0%B8%B2%E0%B8%A2-%E0%B8%AA%E0%B8%A7%E0%B8%A2-%E0%B9%86-%E0%B8%A3%E0%B8%B2%E0%B8%84%E0%B8%B2.png"))
        listCate.add(DanhMuc("Tin xem nhiều", "https://vnexpress.net/rss/tin-xem-nhieu.rss","https://vcdn1-vnexpress.vnecdn.net/2023/06/03/phao-hoa.jpg?w=500&h=300&q=100&dpr=2&fit=crop&s=8omLRP1qYkb4PUVKFt_pwQ"))
        listCate.add(DanhMuc("Tin mới nhất", "https://vnexpress.net/rss/tin-moi-nhat.rss","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSt__cZOyPizzik8BBtY52dbq826f14u81jIPULSeuKOlKkexYHj3uFnXlwEGin7wUo-i4&usqp=CAU"))
        listCate.add(DanhMuc("Thể thao", "https://vnexpress.net/rss/the-thao.rss","https://sohanews.sohacdn.com/zoom/480_300/160588918557773824/2023/6/3/photo1685794358979-16857943591631972040889.jpg"))
        listCate.add(DanhMuc("Giáo dục", "https://vnexpress.net/rss/giao-duc.rss","https://cdn.lawnet.vn/uploads/tintuc/2022/12/20/tai-tro-cho-giao-ducc.jpg"))
        listCate.add(DanhMuc("Kinh doanh", "https://vnexpress.net/rss/kinh-doanh.rss","https://photo-cms-bizlive.epicdn.me/w1200/NSDN/he-lo-ket-qua-kinh-doanh-quy-3-co-doanh-nghiep-da-vuot-xa-chi-tieu-ca-nam-20221004153847_86279.jpg"))

        listCate.add(DanhMuc("Xe", "https://vnexpress.net/rss/oto-xe-may.rss","https://icdn.dantri.com.vn/thumb_w/680/2022/10/05/2024-bugatti-mistral-1664958499760.jpg"))
        listCate.add(DanhMuc("Tâm sự", "https://vnexpress.net/rss/tam-su.rss","https://photo-resize-zmp3.zmdcdn.me/w600_r1x1_jpeg/cover/8/6/6/0/8660e4f26c09947237cf11bdda012a99.jpg"))
    }


    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onResume() {
        super.onResume()
        mHandler.postDelayed(mRunnable, 4000)
    }
}