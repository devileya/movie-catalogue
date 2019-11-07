package com.devileya.moviecatalogue.ui.main.favorite

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
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    companion object {
        fun newInstance(category: String): FavoriteFragment {
            val args = Bundle()
            args.putString("category", category)

            val fragment = FavoriteFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private val viewModel by viewModel<FavoriteViewModel>()

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

    private fun showMovieList(movies: List<DetailModel>?) {
        rv_movie.visibility = View.VISIBLE
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteAdapter(context!!, movies!!){
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DataEnum.DATA.value, it)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun showTvList(tvShows: List<DetailModel>?) {
        rv_movie.visibility = View.VISIBLE
        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = FavoriteAdapter(context!!, tvShows!!){
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DataEnum.DATA.value, it)
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

        viewModel.showLoading.observe(this, Observer {
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.showError.observe(this, Observer {
            Snackbar.make(view!!, it, Snackbar.LENGTH_SHORT)
        })
    }

}
