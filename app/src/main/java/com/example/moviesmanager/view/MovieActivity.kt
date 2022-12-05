package com.example.moviesmanager.view

import android.R.layout.simple_spinner_dropdown_item
import android.R.layout.simple_spinner_item
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.controller.GenreController
import com.example.moviesmanager.databinding.ActivityMovieBinding
import com.example.moviesmanager.model.entity.Genre
import com.example.moviesmanager.model.entity.Model.MOVIE_EDIT
import com.example.moviesmanager.model.entity.Model.MOVIE_EXTRA
import com.example.moviesmanager.model.entity.Movie
import kotlin.random.Random


class MovieActivity : AppCompatActivity() {

    private val binding: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }

    private var selectedGenre: Genre? = null

    private lateinit var genreList: List<Genre>

    private lateinit var genreController: GenreController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        genreController = GenreController(this)

        genreList = genreController.getAll()

        val genreAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            simple_spinner_item,
            genreList.map { it.name }
        )
        genreAdapter.setDropDownViewResource(simple_spinner_dropdown_item)
        binding.genreSp.adapter = genreAdapter
        binding.genreSp.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
                selectedGenre = genreList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedGenre = null
            }
        }

        // Received movie
        val receivedMovie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA)
        val editingMovie = intent.getBooleanExtra(MOVIE_EDIT, false)

        receivedMovie?.let { movie ->
            binding.nameEt.setText(movie.name)
            binding.yearEt.setText(movie.year.toString())
            binding.madeByEt.setText(movie.studio ?: movie.producer ?: "")
            binding.durationEt.setText(movie.duration.toString())
            val genreIndex = genreList.indexOfFirst { it.id == movie.genre.id }
            binding.genreSp.setSelection(genreIndex)
            binding.watchedCb.isChecked = movie.watched
            movie.rating?.let {
                binding.ratingBar.rating = it.toFloat() / 2
                binding.ratingBar.visibility = View.VISIBLE
            }

            if (!editingMovie) {
                binding.nameEt.isEnabled = false
                binding.yearEt.isEnabled = false
                binding.madeByEt.isEnabled = false
                binding.durationEt.isEnabled = false
                binding.genreSp.isEnabled = false
                binding.watchedCb.visibility = View.GONE
                binding.watchedTv.visibility = View.VISIBLE
                if (binding.watchedCb.isChecked) {
                    binding.watchedTv.setText(R.string.watched)
                } else {
                    binding.watchedTv.setText(R.string.not_watched)
                }
                binding.ratingBar.visibility = View.VISIBLE
                binding.ratingBar.setIsIndicator(true)
            }
        }

        // Watched rating interaction
        binding.watchedCb.setOnClickListener {
            if (binding.watchedCb.isChecked) {
                binding.ratingBar.visibility = View.VISIBLE
            } else {
                binding.ratingBar.visibility = View.GONE
            }
        }

        // Save button
        binding.saveBt.setOnClickListener {
            val name = binding.nameEt.text.toString()
            if (name.isBlank()) {
                Toast
                    .makeText(this, R.string.empty_name_toast, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val yearText = binding.yearEt.text.toString()
            if (yearText.isBlank()) {
                Toast
                    .makeText(this, R.string.empty_year_toast, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val madeBy = binding.madeByEt.text.toString()
            if (madeBy.isBlank()) {
                Toast
                    .makeText(this, R.string.empty_made_by_toast, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val durationText = binding.durationEt.text.toString()
            if (durationText.isBlank()) {
                Toast
                    .makeText(this, R.string.empty_duration_toast, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (selectedGenre == null) {
                Toast
                    .makeText(this, R.string.empty_genre_toast, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            val year     = yearText.toInt()
            val duration = durationText.toLong()
            val watched  = binding.watchedCb.isChecked
            val rating   = (binding.ratingBar.rating * 2).toInt()
            val genre    = selectedGenre!!

            val movie = Movie(
                id       = receivedMovie?.id ?: Random(System.currentTimeMillis()).nextInt(),
                name     = name,
                year     = year,
                studio   = madeBy, // TODO studio or producer
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