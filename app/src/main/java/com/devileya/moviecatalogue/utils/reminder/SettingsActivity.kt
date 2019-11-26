package com.devileya.moviecatalogue.utils.reminder

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.utils.DataEnum
import kotlinx.android.synthetic.main.settings_activity.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_title.text = resources.getString(R.string.notification_setting_title)


        reminderReceiver = ReminderReceiver()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val releaseRemainderActive = sharedPreferences.getBoolean(DataEnum.RELEASE.value, true)
        val dailyRemainderActive = sharedPreferences.getBoolean(DataEnum.DAILY.value, true)

        switch_release.isChecked = releaseRemainderActive
        switch_daily.isChecked = dailyRemainderActive

        switch_release.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(DataEnum.RELEASE.value, isChecked).apply()
            when (isChecked) {
                true -> reminderReceiver.setRepeatingReminder(this, DataEnum.RELEASE.value, "08:00")
                false -> reminderReceiver.cancelReminder(this, DataEnum.RELEASE.value)
            }

        }
        switch_daily.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean(DataEnum.DAILY.value, isChecked).apply()
            if (isChecked){
                reminderReceiver.setRepeatingReminder(this, DataEnum.DAILY.value,"07:00", resources.getString(R.string.notify_daily_msg))
            }else{
                reminderReceiver.cancelReminder(this, DataEnum.DAILY.value)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home ->{
            onBackPressed()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}