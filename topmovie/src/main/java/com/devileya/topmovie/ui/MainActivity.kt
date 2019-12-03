package com.devileya.topmovie.ui

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.devileya.topmovie.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerTopMovieAdapter: ViewPagerTopMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        @Suppress("DEPRECATION")
        toolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        toolbar_title.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        initViewPager()
    }

    private fun initViewPager(){
        viewPagerTopMovieAdapter = ViewPagerTopMovieAdapter(supportFragmentManager, this)
        vp_movie_list.adapter = viewPagerTopMovieAdapter

        tab_movie_list.setSelectedTabIndicatorColor(
            ContextCompat.getColor(this,
                R.color.colorPrimary
            ))

        tab_movie_list.setupWithViewPager(vp_movie_list)
    }
}
