package com.devileya.moviecatalogue.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance(category: String): MainFragment{
            val args = Bundle()
            args.putString("category", category)

            val fragment = MainFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var fragmentViewModel: MainFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentViewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
        initViewModel()
    }

    private fun showMovieList(movies: List<MovieModel>?) {
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemMovieAdapter(context!!, movies!!){
                val detailModel = DetailModel(
                    id = it.id,
                    title = it.title,
                    date = it.release_date,
                    synopsis = it.overview,
                    rating = it.vote_average,
                    poster = it.poster_path
                )

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("data", detailModel)
                intent.putExtra("type", "movie")
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun showTvList(tvShows: List<TvShowModel>?) {
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemTvAdapter(context!!, tvShows!!){
                val detailModel = DetailModel(
                    id = it.id,
                    title = it.name,
                    date = it.first_air_date,
                    synopsis = it.overview,
                    rating = it.vote_average,
                    poster = it.poster_path
                )

                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("data", detailModel)
                intent.putExtra("type", "tv")
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun initViewModel() {
        val category = arguments?.getString("category")
        if (category == context?.resources?.getString(R.string.tv_shows)) {
            fragmentViewModel.tvShows.observe(this, Observer {
                showTvList(it)
            })
        } else {
            fragmentViewModel.movies.observe(this, Observer {
                showMovieList(it)
            })
        }

        fragmentViewModel.showLoading.observe(this, Observer {
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}
