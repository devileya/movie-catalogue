package com.devileya.topmovie.ui

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.devileya.topmovie.R
import com.devileya.topmovie.database.DatabaseContract
import com.devileya.topmovie.database.model.DetailModel
import com.devileya.topmovie.utils.DataEnum
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance(category: String): MainFragment {
            val args = Bundle()
            args.putString("category", category)

            val fragment = MainFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initViewModel()
    }

    private fun showMovieList(movies: List<DetailModel>) {
        rv_movie.visibility = View.VISIBLE
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TopMovieAdapter(movies){}
        }
    }

    private fun showTvList(tvShows: List<DetailModel>) {
        rv_movie.visibility = View.VISIBLE
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TopMovieAdapter(tvShows){}
        }
    }

    private fun initViewModel() {

        val cursorMovie = context?.contentResolver?.query(DatabaseContract.FavColumns.CONTENT_URI, null, null, null, null) as Cursor
        val cursorTV = context?.contentResolver?.query(DatabaseContract.FavColumns.CONTENT_URI, null, null, null, null) as Cursor
        viewModel.getMovieList(cursorMovie)
        viewModel.getTvShowList(cursorTV)

        val category = arguments?.getString(DataEnum.CATEGORY.value)
        if (category == context?.resources?.getString(R.string.tv_shows)) {
            viewModel.tvShows.observe(this, Observer {
                if (it.isNotEmpty()) showTvList(it) else tv_no_data.visibility = View.VISIBLE
            })
        } else {
            viewModel.movies.observe(this, Observer {
                if (it.isNotEmpty()) showMovieList(it) else tv_no_data.visibility = View.VISIBLE
            })
        }
    }
}
