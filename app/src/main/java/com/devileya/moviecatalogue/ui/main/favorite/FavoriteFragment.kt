package com.devileya.moviecatalogue.ui.main.favorite

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.ui.detail.DetailActivity
import com.devileya.moviecatalogue.utils.DataEnum
import com.devileya.moviecatalogue.utils.EspressoIdlingResource
import com.devileya.moviecatalogue.utils.widget.ImageBannerWidget
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class FavoriteFragment : Fragment() {

    private lateinit var favoriteAdapter: FavoritePagedAdapter
    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            // Aksi di bawah digunakan untuk melakukan swap ke kenan dan ke kiri
            return makeMovementFlags(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            )
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.adapterPosition
                val detailModel = favoriteAdapter.getItemById(swipedPosition)
                viewModel.deleteFavorite(detailModel!!)
                val snackbar = Snackbar.make(view!!, R.string.message_undo, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.message_ok){ viewModel.insertFavorite(detailModel) }
                snackbar.show()
            }
        }
    })

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
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        favoriteAdapter = FavoritePagedAdapter {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DataEnum.DATA.value, it)
            startActivity(intent)
            activity?.finish()
        }

        rv_movie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
        }

        itemTouchHelper.attachToRecyclerView(rv_movie)

        initViewModel()



//        tvAdapter = FavoritePagedAdapter {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra(DataEnum.DATA.value, it)
//            startActivity(intent)
//            activity?.finish()
//        }

    }


    private fun initViewModel() {
        val category = arguments?.getString(DataEnum.CATEGORY.value)
        if (category == context?.resources?.getString(R.string.tv_shows)) {
            viewModel.tvShowsPagination?.observe(this, Observer {
                Timber.d("tv data ${it.size} ${rv_movie.adapter?.itemCount}")
                favoriteAdapter.submitList(it)
            })
        } else {
            viewModel.moviesPagination?.observe(this, Observer {
                favoriteAdapter.submitList(it)
                Timber.d("movie data ${it.size} ${rv_movie.adapter?.itemCount}")
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
