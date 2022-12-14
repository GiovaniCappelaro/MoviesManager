package com.example.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.adapter.MovieAdapter
import com.example.moviesmanager.controller.GenreController
import com.example.moviesmanager.controller.MovieController
import com.example.moviesmanager.databinding.ActivityMovieManagerBinding
import com.example.moviesmanager.model.entity.Genre
import com.example.moviesmanager.model.entity.Model.MOVIE_EDIT
import com.example.moviesmanager.model.entity.Model.MOVIE_EXTRA
import com.example.moviesmanager.model.entity.Movie

class MoviesManagerActivity : AppCompatActivity() {

    private val binding: ActivityMovieManagerBinding by lazy {
        ActivityMovieManagerBinding.inflate(layoutInflater)
    }

    private lateinit var movieList: MutableList<Movie>

    private lateinit var movieAdapter: MovieAdapter

    private lateinit var arl: ActivityResultLauncher<Intent>

    private lateinit var genreController: GenreController
    private lateinit var movieController: MovieController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // mockMovieList()

        genreController = GenreController(this)
        movieController = MovieController(this, genreController)

        movieList = movieController.getAll()

        movieAdapter = MovieAdapter(this, movieList)
        binding.moviesLv.adapter = movieAdapter

        val splitFab: View = findViewById(R.id.addFab)
        splitFab.setOnClickListener {
            addMovie()
        }

        arl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val movie = result.data?.getParcelableExtra<Movie>(MOVIE_EXTRA)

                movie?.let { m ->
                    val pos = movieList.indexOfFirst { it.id == m.id }

                    if (pos == -1) {
                        m.id = movieController.insert(m)
                        movieList.add(m)
                    } else {
                        movieController.edit(m)
                        movieList[pos] = m
                    }

                    movieAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(binding.moviesLv)

        binding.moviesLv.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewMovie(position)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.movie_manager_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addMovieMi -> {
                addMovie()
                true
            }
            R.id.editGenresMi -> {
                editGenres()
                true
            }
            R.id.orderByNameMi -> {
                orderMoviesByName()
                true
            }
            R.id.orderByRating -> {
                orderMoviesByRating()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.movie_ctx_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

        return when (item.itemId) {
            R.id.removeMovieMi -> {
                removeMovie(position)
                true
            }
            R.id.editMovieMi -> {
                editMovie(position)
                true
            }
            else -> {
                false
            }
        }
    }

    fun addMovie() {
        val intent = Intent(this, MovieActivity::class.java)
        arl.launch(intent)
    }

    fun editMovie(position: Int) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra(MOVIE_EXTRA, movieList[position])
        intent.putExtra(MOVIE_EDIT, true)
        arl.launch(intent)
    }

    fun removeMovie(position: Int) {
        val id = movieList[position].id
        movieController.remove(id)
        movieList.removeAt(position)
        movieAdapter.notifyDataSetChanged()
    }

    fun viewMovie(position: Int) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra(MOVIE_EXTRA, movieList[position])
        arl.launch(intent)
    }

    fun editGenres() {
        val intent = Intent(this, GenreListActivity::class.java)
        arl.launch(intent)
    }

    fun orderMoviesByName() {
        movieList.sortBy { it.name }
        movieAdapter.notifyDataSetChanged()
    }

    fun orderMoviesByRating() {
        movieList.sortByDescending { it.rating }
        movieAdapter.notifyDataSetChanged()
    }

    fun mockMovieList() {
        for (i in 1..6) {
            movieList.add(
                Movie(
                    id = i,
                    name = "Movie " + i,
                    year = 2000,
                    studio = "Studio",
                    producer = null,
                    duration = 120,
                    watched = false,
                    rating = i,
                    genre = Genre(0, "A????o"),
                )
            )
        }
    }
}