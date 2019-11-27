package com.devileya.moviecatalogue.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.preference.PreferenceManager
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.ui.main.favorite.FavoriteActivity
import com.devileya.moviecatalogue.ui.main.search.SearchActivity
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.reminder.ReminderReceiver
import com.devileya.moviecatalogue.utils.reminder.SettingsActivity
import kotlinx.android.synthetic.main.main_activity.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var sharedPreferences: SharedPreferences
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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val releaseReminderActive = sharedPreferences.getBoolean(DataEnum.RELEASE.value, true)
        val dailyReminderActive = sharedPreferences.getBoolean(DataEnum.DAILY.value, true)

        reminderReceiver = ReminderReceiver()

        val isReleaseReminderActive = reminderReceiver.isReminderSet(this, DataEnum.RELEASE.value )
        val isDailyReminderActive = reminderReceiver.isReminderSet(this, DataEnum.DAILY.value )

        Timber.d("release reminder $isReleaseReminderActive status $releaseReminderActive")
        Timber.d("daily reminder $isDailyReminderActive status $dailyReminderActive")


        if(!isDailyReminderActive && dailyReminderActive){
            reminderReceiver.setRepeatingReminder(this, DataEnum.DAILY.value,"07:00", resources.getString(R.string.notify_daily_msg))
        }

        if(!isReleaseReminderActive && releaseReminderActive){
            reminderReceiver.setRepeatingReminder(this, DataEnum.RELEASE.value,"08:00", resources.getString(R.string.notify_daily_msg))
        }
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
        val pairTransition = Pair.create<View, String>(toolbar, "container")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairTransition)
        when (item.itemId) {
            R.id.action_change_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.action_favorite -> {
                startActivity(Intent(this, FavoriteActivity::class.java), options.toBundle())
            }
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java), options.toBundle())
            }
            R.id.action_reminder_setting -> {
                startActivity(Intent(this, SettingsActivity::class.java), options.toBundle())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
