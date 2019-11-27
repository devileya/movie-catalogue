package com.devileya.moviecatalogue.ui.main.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_movie.view.*

/**
 * Created by Arif Fadly Siregar 2019-11-27.
 */
class FavoritePagedAdapter(private val listener: (DetailModel) -> Unit): PagedListAdapter<DetailModel, FavoritePagedAdapter.FavoriteViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        getItem(position)?.let { holder.bindItem(it, listener) }
    }

    inner class FavoriteViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(
            favorite: DetailModel,
            listener: (DetailModel) -> Unit
        ) {
            setIsRecyclable(false)
            itemView.tv_title.text = favorite.title
            itemView.tv_popularity.text = String.format(
                itemView.context.resources.getString(R.string.popularity),
                favorite.popularity
            )
            itemView.tv_rate.text = String.format(
                itemView.context.resources.getString(R.string.rating),
                favorite.rating
            )
            itemView.tv_date.text =
                String.format(itemView.context.resources.getString(R.string.date), favorite.date)
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w154/${favorite.poster}")
                .into(itemView.iv_movie)
            containerView.setOnClickListener { listener(favorite) }
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<DetailModel>() {
    override fun areItemsTheSame(oldItem: DetailModel, newItem: DetailModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DetailModel, newItem: DetailModel): Boolean {
        return oldItem.id == newItem.id
    }

}