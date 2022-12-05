package com.example.moviesmanager.model.dao

import com.example.moviesmanager.model.entity.MovieEntity

interface MovieEntityDao {

    fun create(movie: MovieEntity): Int

    fun retrieve(id: Int): MovieEntity?

    fun retrieveAll(): MutableList<MovieEntity>

    fun update(movie: MovieEntity): Int

    fun delete(id: Int): Int
}