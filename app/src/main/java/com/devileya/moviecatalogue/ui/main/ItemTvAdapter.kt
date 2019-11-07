package com.devileya.moviecatalogue.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.TvShowModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_movie.view.*

class ItemTvAdapter(
    private val context: Context, val tvShows: List<TvShowModel>, val listener: (TvShowModel) -> Unit)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ItemTvAdapter.MovieViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bindItem(context, tvShows[position], listener)
    }

    override fun getItemCount(): Int = tvShows.size


    inner class MovieViewHolder(override val containerView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(
            context: Context,
            tvShow: TvShowModel,
            listener: (TvShowModel) -> Unit
        ) {
            setIsRecyclable(false)
            itemView.tv_title.text = tvShow.name
            itemView.tv_popularity.text = String.format(context.resources.getString(R.string.popularity), tvShow.popularity)
            itemView.tv_rate.text = String.format(context.resources.getString(R.string.rating), tvShow.vote_average)
            itemView.tv_date.text = String.format(context.resources.getString(R.string.date), tvShow.first_air_date)
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154/${tvShow.poster_path}")
                .into(itemView.iv_movie)
            containerView.setOnClickListener { listener(tvShow) }
        }
    }
}