package com.example.moviesmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.moviesmanager.R
import com.example.moviesmanager.model.Movie

class MovieAdapter(
    context: Context,
    private val movieList: MutableList<Movie>
) : ArrayAdapter<Movie>(context, R.layout.tile_movie, movieList) {

    private data class TileMovieHolder(
        val nameTv: TextView,
        val yearTv: TextView,
        val madeByTv: TextView,
        val durationTv: TextView,
        val watchedTv: TextView, // TODO change to checkbox ?
        val ratingBar: RatingBar,
        val genreTv: TextView,
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val movie = movieList[position]

        var movieTileView = convertView

        if (movieTileView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            movieTileView = inflater.inflate(
                R.layout.tile_movie,
                parent,
                false
            )

            val tileMovieHolder = TileMovieHolder(
                movieTileView.findViewById(R.id.nameTv),
                movieTileView.findViewById(R.id.yearTv),
                movieTileView.findViewById(R.id.madeByTv),
                movieTileView.findViewById(R.id.durationTv),
                movieTileView.findViewById(R.id.watchedTv),
                movieTileView.findViewById(R.id.ratingBar),
                movieTileView.findViewById(R.id.genreTv),
            )

            movieTileView.tag = tileMovieHolder
        }

        with(movieTileView?.tag as TileMovieHolder) {
            nameTv.text = movie.name
            genreTv.text = movie.genre.name
            yearTv.text = movie.year.toString()
            madeByTv.text = movie.studio ?: movie.producer ?: ""
            durationTv.text = minutesToDurationString(movie.duration)
            if (movie.rating == null) {
                ratingBar.isVisible = false;
            } else {
                ratingBar.rating = movie.rating.toFloat() / 2
            }
            if (movie.watched) {
                watchedTv.text = context.getString(R.string.watched)
            } else {
                watchedTv.text = ""
            }
        }

        return movieTileView
    }

    private fun minutesToDurationString(duration: Long) : String {
        if (duration <= 60L) {
            return String.format(context.getString(R.string.minutes), duration)
        }

        val hours   = duration/60
        val minutes = duration%60

        val hoursStr = String.format(context.getString(R.string.hours), hours)

        if (minutes == 0L) {
            return hoursStr
        }

        val minutesStr = String.format(context.getString(R.string.minutes), minutes)

        return "$hoursStr $minutesStr"
    }
}