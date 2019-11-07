package com.devileya.moviecatalogue.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.devileya.moviecatalogue.R
import com.devileya.moviecatalogue.network.model.MovieModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_movie.view.*

class ItemMovieAdapter(
    private val context: Context, val movies: List<MovieModel>, val listener: (MovieModel) -> Unit)
    : androidx.recyclerview.widget.RecyclerView.Adapter<ItemMovieAdapter.MovieViewHolder>()
    {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_movie, parent, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            holder.setIsRecyclable(false)
            holder.bindItem(context, movies[position], listener)
        }

        override fun getItemCount(): Int = movies.size


    inner class MovieViewHolder(override val containerView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindItem(
            context: Context,
            movie: MovieModel,
            listener: (MovieModel) -> Unit
        ) {
            setIsRecyclable(false)
            itemView.tv_title.text = movie.title
            itemView.tv_popularity.text = String.format(context.resources.getString(R.string.popularity), movie.popularity)
            itemView.tv_rate.text = String.format(context.resources.getString(R.string.rating), movie.vote_average)
            itemView.tv_date.text = String.format(context.resources.getString(R.string.date), movie.release_date)
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w154/${movie.poster_path}")
                .into(itemView.iv_movie)
            containerView.setOnClickListener { listener(movie) }
        }
    }
}