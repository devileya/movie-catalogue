package com.devileya.moviecatalogue.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.devileya.moviecatalogue.BuildConfig
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.ui.main.MainActivity
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    private val viewModel by viewModel<DetailViewModel>()
    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null
    private lateinit var youTubePlayer: YouTubePlayer
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val data = intent.getParcelableExtra<DetailModel>(DataEnum.DATA.value)
        toolbar_title.text = data?.title
        iv_poster.z = 5f
        tv_date.text = data?.date
        tv_synopsis.text = data?.synopsis
        val rating = (data?.rating!!.toFloat()*10).toInt()
        tv_score.text = String.format("%s%%", rating)
        rb_score.rating = (rating/20f)
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w185${data.poster}")
            .placeholder(R.drawable.img_placeholder)
            .into(iv_poster)

        initViewModel(data)

        btn_favorite.setOnClickListener {
            when (isFavorite) {
                true -> viewModel.deleteFavorite(data)
                false -> viewModel.insertFavorite(data)
            }
        }
    }

    private fun initYoutubePlayer(key: String?){
        youTubePlayerFragment = supportFragmentManager.findFragmentById(R.id.vv_trailer) as YouTubePlayerSupportFragment?
        youTubePlayerFragment?.initialize(BuildConfig.API_KEY, object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider, player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    youTubePlayer = player
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                    youTubePlayer.cueVideo(key)
                    youTubePlayer.setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
                        override fun onPlaying() { iv_poster.z = 0f }
                        override fun onPaused() { iv_poster.z = 5f }
                        override fun onStopped() { iv_poster.z = 5f }
                        override fun onBuffering(b: Boolean) {}
                        override fun onSeekTo(i: Int) {}
                    })
                }
            }

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {
                Timber.e("Something went wrong")
            }
        })
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

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun initViewModel(data: DetailModel){
        EspressoIdlingResource.increment()
        viewModel.getFavoriteById(data.id)
        viewModel.getVideoTrailer(data.id, data.category)

        viewModel.favorite.observe(this, Observer {
            if (it != null) {
                isFavorite = true
                val ivAnimation = AnimatedVectorDrawableCompat.create(iv_heart.context, R.drawable.ic_heart_anim)
                iv_heart.setImageDrawable(ivAnimation)
                ivAnimation?.start()
                iv_heart.imageTintList = getColorStateList(R.color.red)
            } else {
                iv_heart.setImageDrawable(getDrawable(R.drawable.ic_heart))
                iv_heart.imageTintList = getColorStateList(R.color.grey)
                isFavorite = false
        }
        })

        viewModel.showLoading.observe(this, Observer {
            progress_bar_detail.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.videos.observe(this, Observer {
            initYoutubePlayer(it[0].key)
            EspressoIdlingResource.decrement()
        })
    }
}
