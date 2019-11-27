package com.devileya.moviecatalogue.ui.main.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.DetailModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_movie.view.*

/**
 * Created by Arif Fadly Siregar 2019-11-08.
 */
class FavoriteAdapter(
    private val context: Context, private val favorites: List<DetailModel>, private val listener: (DetailModel) -> Unit)
    : androidx.recyclerview.widget.RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bindItem(context, favorites[position], listener)
    }

    override fun getItemCount(): Int = favorites.size


    inner class FavoriteViewHolder(override val containerView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(
            context: Context,
            favorite: DetailModel,
            listener: (DetailModel) -> Unit
        ) {
            setIsRecyclable(false)
            itemView.tv_title.text = favorite.title
            itemView.tv_popularity.text = String.format(context.resources.getString(R.string.popularity), favorite.popularity)
            itemView.tv_rate.text = String.format(context.resources.getString(R.string.rating), favorite.rating)
            itemView.tv_date.text = String.format(context.resources.getString(R.string.date), favorite.date)
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154/${favorite.poster}")
                .into(itemView.iv_movie)
            containerView.setOnClickListener { listener(favorite) }
        }
    }
}