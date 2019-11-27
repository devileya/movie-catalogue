package com.devileya.moviecatalogue.ui.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import com.devileya.moviecatalogue.utils.widget.ImageBannerWidget
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance(category: String): MainFragment{
            val args = Bundle()
            args.putString(DataEnum.CATEGORY.value, category)

            val fragment = MainFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private val viewModel by viewModel<MainFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    private fun showMovieList(movies: List<MovieModel>?) {
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
                activity?.finish()
            }
        }
    }

    private fun showTvList(tvShows: List<TvShowModel>?) {
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
                activity?.finish()
            }
        }
    }

    private fun initViewModel() {
        EspressoIdlingResource.increment()
        val category = arguments?.getString(DataEnum.CATEGORY.value)
        if (category == context?.resources?.getString(R.string.tv_shows)) {
            viewModel.tvShows.observe(this, Observer {
                if (it.isNotEmpty()) showTvList(it) else tv_no_data.visibility = View.VISIBLE
                EspressoIdlingResource.decrement()
            })
        } else {
            viewModel.movies.observe(this, Observer {
                if (it.isNotEmpty()) showMovieList(it) else tv_no_data.visibility = View.VISIBLE
                EspressoIdlingResource.decrement()
            })
        }

        viewModel.favorites.observe(this, Observer {
            val intent = Intent(activity, ImageBannerWidget::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context!!, ImageBannerWidget::class.java))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            context?.sendBroadcast(intent)
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.stack_view)
        })

        viewModel.showLoading.observe(this, Observer {
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.showError.observe(this, Observer {
            Snackbar.make(view!!, it, Snackbar.LENGTH_SHORT)
        })
    }
}
