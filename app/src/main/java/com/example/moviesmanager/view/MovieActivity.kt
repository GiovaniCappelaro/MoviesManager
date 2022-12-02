package com.example.moviesmanager.view

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.databinding.ActivityMovieBinding
import com.example.moviesmanager.model.Genre
import com.example.moviesmanager.model.Model.MOVIE_EDIT
import com.example.moviesmanager.model.Model.MOVIE_EXTRA
import com.example.moviesmanager.model.Movie
import kotlin.random.Random


class MovieActivity: AppCompatActivity() {

    private val binding: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }

    private val selectedGenre: Genre? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val receivedMovie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA)
        val editingMovie = intent.getBooleanExtra(MOVIE_EDIT, false)
        val ctx = this;

        // TODO dinamic
        val genreList = mutableListOf(
            Genre(Random(System.currentTimeMillis()).nextInt(), "Ação"),
            Genre(Random(System.currentTimeMillis()).nextInt(), "Aventura"),
            Genre(Random(System.currentTimeMillis()).nextInt(), "Romace"),
            Genre(Random(System.currentTimeMillis()).nextInt(), "Terror"),
        )

        val genreAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_item,
            genreList.map { it.name }
        )
        genreAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.genreSp.adapter = genreAdapter

        binding.saveBt.setOnClickListener {
            // TODO tratar empty
            val year     = binding.yearEt.text.toString().toInt()
            val duration = binding.durationEt.text.toString().toLong()
            val watched  = binding.watchedCb.isActivated
            val rating   = (binding.ratingBar.rating * 2).toInt()
            val genre    = Genre(0, "Ação")

            val movie = Movie(
                id       = receivedMovie?.id ?: Random(System.currentTimeMillis()).nextInt(),
                name     = binding.nameEt.text.toString(),
                year     = year,
                studio   = binding.madeByEt.text.toString(), // TODO studio or producer
                producer = null,
                duration = duration,
                watched  = watched,
                rating   = rating,
                genre    = genre,
            )

            val resultIntent = Intent()
            resultIntent.putExtra(MOVIE_EXTRA, movie)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}