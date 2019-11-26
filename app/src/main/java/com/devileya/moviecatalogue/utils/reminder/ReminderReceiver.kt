package com.devileya.moviecatalogue.utils.reminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.domain.repository.DataRepository
import com.devileya.moviecatalogue.network.message.response.MovieResponse
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.UseCaseResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Arif Fadly Siregar 2019-11-27.
 */
class ReminderReceiver : BroadcastReceiver(), KoinComponent {
    
    private val dataRepository by inject<DataRepository>()

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        const val ID_RELEASE = 100
        const val ID_DAILY = 101

        private const val TIME_FORMAT = "HH:mm"

    }

    override fun onReceive(context: Context, intent: Intent) {

        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: ""

        if (type.equals(DataEnum.RELEASE.value, ignoreCase = true)){
            loadReleaseMovie(context)
        }
        if (type.equals(DataEnum.DAILY.value, ignoreCase = true)){
            showReminderNotification(context, message)
        }
    }


    private fun loadReleaseMovie(ctx: Context) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        
        GlobalScope.launch {
            when (val result = dataRepository.getReleaseMovie(currentDate)) {
                is UseCaseResult.Success<MovieResponse> -> {
                    val dataDetail = convertMovieToDetailModel(result.data.results)
                    showReleaseReminderNotification(ctx, dataDetail)
                }
                is UseCaseResult.Error -> Timber.e("Error Reminder ${result.exception.message}")
            }
        }
    }




    private fun showReminderNotification(context: Context, message: String) {

        val channelId = "Channel_1"
        val channelName = "DailyReminder channel"
        val title = "Movie Catalogue"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val reminderSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        setChannelNotification(channelId, channelName, notificationManagerCompat)
        setNotificationContent(context, channelId, ID_DAILY, title, message, notificationManagerCompat, reminderSound)
    }

    private fun showReleaseReminderNotification(context: Context, listMovie: List<DetailModel>?) {
        val channelId = "Channel_2"
        val channelName = "ReleaseReminder channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val reminderSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        setChannelNotification(channelId, channelName, notificationManagerCompat)

        listMovie?.forEach {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DataEnum.DATA.value, it)
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, it.id.toInt(), intent, 0)
            val message = "${it.title} has been released today!"

            setNotificationContent(context, channelId, it.id.toInt(), it.title, message, notificationManagerCompat, reminderSound, pendingIntent)
        }


    }


    fun setRepeatingReminder(context: Context, type: String, time: String, message: String? = null) {

        if (isDateInvalid(time)) return

        val reminderManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val now = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val reminderTime = if (calendar.timeInMillis <= now.timeInMillis) calendar.timeInMillis+ (AlarmManager.INTERVAL_DAY+1) else calendar.timeInMillis



        val requestCode = if (type.equals(DataEnum.RELEASE.value, ignoreCase = true)) ID_RELEASE else ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        reminderManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, reminderTime, AlarmManager.INTERVAL_DAY, pendingIntent)

    }


    fun cancelReminder(context: Context, type: String) {
        val reminderManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(DataEnum.RELEASE.value, ignoreCase = true)) ID_RELEASE else ID_DAILY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        reminderManager.cancel(pendingIntent)
    }


    fun isReminderSet(context: Context, type: String): Boolean {
        val intent = Intent(context, ReminderReceiver::class.java)
        val requestCode = if (type.equals(DataEnum.RELEASE.value, ignoreCase = true)) ID_RELEASE else ID_DAILY

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }

    private fun isDateInvalid(date: String): Boolean {
        return try {
            val df = SimpleDateFormat(TIME_FORMAT, Locale.ENGLISH)
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    private fun convertMovieToDetailModel(movies: List<MovieModel>): List<DetailModel> {
        val detailModel = mutableListOf<DetailModel>()
        movies.forEach {
            detailModel.add(
                DetailModel(
                    id = it.id!!,
                    title = it.title,
                    date = it.release_date,
                    synopsis = it.overview,
                    rating = it.vote_average,
                    poster = it.poster_path,
                    category = DataEnum.MOVIE.value,
                    popularity = it.popularity
                )
            )
        }
        return detailModel
    }

    private fun setChannelNotification(channelId: String, channelName: String, notificationManagerCompat: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            notificationManagerCompat.createNotificationChannel(channel)
        }
    }

    private fun setNotificationContent(context: Context, channelId: String, id: Int, title: String?, message: String?, notificationManagerCompat: NotificationManager, reminderSound: Uri, pendingIntent: PendingIntent? = null) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(reminderSound)
            .setContentIntent(pendingIntent)

        builder.setChannelId(channelId)

        val notification = builder.build()

        notificationManagerCompat.notify(id, notification)
    }
}