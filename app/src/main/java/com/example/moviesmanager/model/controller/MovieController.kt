package com.example.moviesmanager.controller

import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.model.dao.MovieEntityDao
import com.example.moviesmanager.model.entity.Movie
import com.example.moviesmanager.model.entity.MovieEntity
import com.example.moviesmanager.model.persistence.MovieEntityDaoSqlite

class MovieController(private val activity: AppCompatActivity, private val genreController: GenreController) {

    private val movieEntityDaoImpl: MovieEntityDao = MovieEntityDaoSqlite(activity)

    private fun Movie.toEntity() = MovieEntity(
        id,
        name,
        year,
        studio,
        producer,
        duration,
        watched,
        rating,
        genre.id,
    )

    private fun MovieEntity.toModel(): Movie? {
        genreController.get(genreId)?. let {
            return Movie(
                id,
                name,
                year,
                studio,
                producer,
                duration,
                watched,
                rating,
                it,
            )
        }

        return null
    }

    fun insert(movie: Movie) = movieEntityDaoImpl.create(movie.toEntity())

    fun get(id: Int) = movieEntityDaoImpl.retrieve(id)?.toModel()

    fun getAll() = movieEntityDaoImpl.retrieveAll().mapNotNull { it.toModel() }.toMutableList()

    fun edit(movie: Movie) = movieEntityDaoImpl.update(movie.toEntity())

    fun remove(id: Int) = movieEntityDaoImpl.delete(id)
}