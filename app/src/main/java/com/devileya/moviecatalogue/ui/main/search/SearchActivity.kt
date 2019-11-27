package com.devileya.moviecatalogue.ui.main.search

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import com.devileya.moviecatalogue.ui.main.ItemMovieAdapter
import com.devileya.moviecatalogue.ui.main.ItemTvAdapter
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import com.devileya.moviecatalogue.utils.widget.ImageBannerWidget
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.rv_movie
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class SearchActivity : AppCompatActivity() {

    private lateinit var movieAdapter: ItemMovieAdapter
    private lateinit var tvAdapter: ItemTvAdapter
    private val viewModel by inject<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        main_content.visibility = View.GONE

        initViewModel()

        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(et_search.text.toString())
                    return true
                }
                return false
            }
        })

        btn_search.setOnClickListener {
            performSearch(et_search.text.toString())
        }

        btn_expand_movie.setOnClickListener {
            rv_movie.visibility = if(rv_movie.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            iv_arrow_movie.rotation = if(rv_movie.visibility == View.VISIBLE) 0f  else 180f
        }

        btn_expand_tv.setOnClickListener {
            rv_tv.visibility = if(rv_tv.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            iv_arrow_tv.rotation = if(rv_tv.visibility == View.VISIBLE) 0f  else 180f
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

    fun performSearch(keyWord: String){
        hideKeyboard(this)
        viewModel.searchMovie(keyWord)
        viewModel.searchTVShows(keyWord)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun updateWidget(){
        val intent = Intent(this@SearchActivity, ImageBannerWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val appWidgetManager = AppWidgetManager.getInstance(this@SearchActivity)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this@SearchActivity, ImageBannerWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
        appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
    }

    private fun showMovieList(movies: List<MovieModel>?) {
        rv_movie.isNestedScrollingEnabled = false
        rv_movie.visibility = View.VISIBLE
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemMovieAdapter(movies!!){
                val detailModel = DetailModel(
                    id = it.id!!,
                    title = it.title,
                    date = it.release_date,
                    synopsis = it.overview,
                    rating = it.vote_average,
                    poster = it.poster_path,
                    category = DataEnum.MOVIE.value,
                    popularity = it.popularity
                )

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DataEnum.DATA.value, detailModel)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun showTvList(tvShows: List<TvShowModel>?) {
        rv_tv.isNestedScrollingEnabled = false
        rv_movie.visibility = View.VISIBLE
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemTvAdapter(tvShows!!){
                val detailModel = DetailModel(
                    id = it.id,
                    title = it.name,
                    date = it.first_air_date,
                    synopsis = it.overview,
                    rating = it.vote_average,
                    poster = it.poster_path,
                    category = DataEnum.TV.value,
                    popularity = it.popularity
                )

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DataEnum.DATA.value, detailModel)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun initViewModel() {
        EspressoIdlingResource.increment()

        viewModel.tvShows.observe(this, Observer {
            if (it != null) {
                showTvList(it)
                Timber.d("TvFragment $it")
            }
            tv_text_result_tv.text = String.format("%d TV shows found", tvAdapter.itemCount)
            main_content.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.GONE else View.VISIBLE
            view_no_data.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.VISIBLE else View.GONE
            updateWidget()
                EspressoIdlingResource.decrement()
            })

        viewModel.movies.observe(this, Observer {
            if (it != null) {
                showMovieList(it)
                progressBar.visibility = View.GONE
                Timber.d("MovieData $it")
            }
            tv_text_result_movie.text = String.format("%d movies found", movieAdapter.itemCount)
            main_content.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.GONE else View.VISIBLE
            view_no_data.visibility = if(movieAdapter.itemCount==0 && tvAdapter.itemCount==0) View.VISIBLE else View.GONE
            updateWidget()
            EspressoIdlingResource.decrement()
        })

        viewModel.showLoading.observe(this, Observer {
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.showError.observe(this, Observer {
            Snackbar.make(root_layout, it, Snackbar.LENGTH_SHORT)
        })
    }
}
