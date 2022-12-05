package com.example.moviesmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.moviesmanager.R
import com.example.moviesmanager.model.entity.Genre

class GenreAdapter(
    context: Context,
    private val genreList: MutableList<Genre>
) : ArrayAdapter<Genre>(context, R.layout.tile_genre, genreList) {

    private data class TileGenreHolder(
        val nameTv: TextView,
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val genre = genreList[position]

        var genreTileView = convertView

        if (genreTileView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            genreTileView = inflater.inflate(
                R.layout.tile_genre,
                parent,
                false
            )

            val tileGenreHolder = TileGenreHolder(
                genreTileView.findViewById(R.id.nameTv),
            )

            genreTileView.tag = tileGenreHolder
        }

        with(genreTileView?.tag as TileGenreHolder) {
            nameTv.text = genre.name
        }

        return genreTileView
    }
}