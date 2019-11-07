package com.devileya.moviecatalogue.ui.main

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.ui.main.favorite.FavoriteActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerMovieAdapter: ViewPagerMovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        @Suppress("DEPRECATION")
        toolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
        toolbar_title.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        initViewPager()
    }

    private fun initViewPager(){
        viewPagerMovieAdapter = ViewPagerMovieAdapter(supportFragmentManager, this)
        vp_movie_list.adapter = viewPagerMovieAdapter

        tab_movie_list.setSelectedTabIndicatorColor(ContextCompat.getColor(this,
            R.color.colorPrimary
        ))

        tab_movie_list.setupWithViewPager(vp_movie_list)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
