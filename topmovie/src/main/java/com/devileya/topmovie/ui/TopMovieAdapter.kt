package com.devileya.topmovie.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devileya.topmovie.R
import com.devileya.topmovie.database.model.DetailModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_movie.view.*

/**
 * Created by Arif Fadly Siregar 2019-12-03.
 */
class TopMovieAdapter(private val favorites: List<DetailModel>, private val listener: (DetailModel) -> Unit)
    : androidx.recyclerview.widget.RecyclerView.Adapter<TopMovieAdapter.TopMovieViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
        return TopMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopMovieViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bindItem(favorites[position], listener)
    }

    override fun getItemCount(): Int = favorites.size


    inner class TopMovieViewHolder(override val containerView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(
            favorite: DetailModel,
            listener: (DetailModel) -> Unit
        ) {
            setIsRecyclable(false)
            itemView.tv_title.text = favorite.title
            itemView.tv_popularity.text = String.format(itemView.context.resources.getString(R.string.popularity), favorite.popularity)
            itemView.tv_rate.text = String.format(itemView.context.resources.getString(R.string.rating), favorite.rating)
            itemView.tv_date.text = String.format(itemView.context.resources.getString(R.string.date), favorite.date)
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w154/${favorite.poster}")
                .into(itemView.iv_movie)
            containerView.setOnClickListener { listener(favorite) }
        }
    }
}