package com.devileya.moviecatalogue.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.devileya.moviecatalogue.BuildConfig
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.ui.main.MainActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null
    private lateinit var youTubePlayer: YouTubePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val data = intent.getParcelableExtra<DetailModel>("data")
        val type = intent.getStringExtra("type")
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

        viewModel.getVideoTrailer(data.id, type)
        viewModel.videoResponse.observe(this, Observer {
            initYoutubePlayer(it.results?.get(0)?.key)
        })
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
                Log.e("DetailActivity", "Something went wrong")
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
}
