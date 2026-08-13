package com.example.netflixclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    private val movies: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.posterImage)
        private val title: TextView = itemView.findViewById(R.id.titleText)

        fun bind(movie: Movie) {
            title.text = movie.titre
            if (movie.poster != null && movie.poster.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load("file:///android_asset/posters/${movie.poster}")
                    .placeholder(R.drawable.placeholder)
                    .into(poster)
            } else {
                poster.setImageResource(R.drawable.placeholder)
            }
            itemView.setOnClickListener { onClick(movie) }
        }
    }
}
