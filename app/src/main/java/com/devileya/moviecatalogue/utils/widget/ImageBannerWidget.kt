package com.devileya.moviecatalogue.utils.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.CATEGORY
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.DATE
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.ID
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.POPULARITY
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.POSTER
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.RATE
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.SYNOPSIS
import com.devileya.moviecatalogue.database.DatabaseContract.FavColumns.Companion.TITLE
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import com.devileya.moviecatalogue.utils.DataEnum

/**
 * Created by Arif Fadly Siregar 2019-11-27.
 */
class ImageBannerWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if(intent?.action !=null){
            if(intent.action == TOAST_ACTION){

                val id = intent.getStringExtra(ID) ?: ""
                val title = intent.getStringExtra(TITLE)
                val date = intent.getStringExtra(DATE)
                val rating = intent.getStringExtra(RATE)
                val synopsis = intent.getStringExtra(SYNOPSIS)
                val poster = intent.getStringExtra(POSTER)
                val category = intent.getStringExtra(CATEGORY)
                val popularity = intent.getStringExtra(POPULARITY)

                val data = DetailModel(id = id, rating = rating, poster = poster, synopsis = synopsis, title = title, date = date, category = category, popularity = popularity)
                val newIntent = Intent(context, DetailActivity::class.java)
                newIntent.putExtra(DataEnum.DATA.value, data)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(newIntent)


            }
        }


    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        private const val TOAST_ACTION = "com.im.layarngaca21.TOAST_ACTION"
        const val EXTRA_ITEM = "com.im.layarngaca21.EXTRA_ITEM"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val views = RemoteViews(context.packageName, R.layout.image_banner_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)
            val toastIntent = Intent(context, ImageBannerWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext)
    }

}