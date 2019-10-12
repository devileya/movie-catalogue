package com.devileya.moviecatalogue.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.MovieModel
import com.devileya.moviecatalogue.network.model.TvShowModel
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
//        fragmentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainFragmentViewModel::class.java)
        fragmentViewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
        val category = arguments?.getString("category")


        val movie = MovieModel(
            popularity = "123132",
            vote_count = "23123",
            title = "adsadasd",
            vote_average = "32131",
            orginal_title = "asdasd",
            video = false,
            poster_path = "asdasd",
            id = "asdasd",
            adult = "asdasd",
            backdrop_path = "adsada",
            original_language = "adsad",
            genre_ids = listOf("asdasd"),
            overview = "asdadsa",
            release_date = "asdasd")
        val movies = fragmentViewModel.getMovieList()
        val tvShows = fragmentViewModel.getTvShowList()
        Log.d("moviess","movie $movies")
//        val rvMovie = view!!.findViewById<RecyclerView>(R.id.rv_movie)
//        rv_movie.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = ItemMovieAdapter(context!!, movies!!){}
//        }

        if (category == context?.resources?.getString(R.string.tv_shows))
            showTvList(tvShows)
        else
            showMovieList(movies)
    }

    fun showMovieList(movies: List<MovieModel>?) {
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemMovieAdapter(context!!, movies!!){}
        }
    }

    fun showTvList(tvShows: List<TvShowModel>?) {
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemTvAdapter(context!!, tvShows!!){}
        }
    }

}
