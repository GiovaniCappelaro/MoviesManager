package com.example.moviesmanager.controller

import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.model.dao.GenreDao
import com.example.moviesmanager.model.entity.Genre
import com.example.moviesmanager.model.persistence.GenreDaoSqlite

class GenreController(private val activity: AppCompatActivity) {

    private val genreDaoImpl: GenreDao = GenreDaoSqlite(activity)

    fun insert(genre: Genre) = genreDaoImpl.create(genre)

    fun get(id: Int) = genreDaoImpl.retrieve(id)

    fun getAll() = genreDaoImpl.retrieveAll()

    fun edit(genre: Genre) = genreDaoImpl.update(genre)

    fun remove(id: Int) = genreDaoImpl.delete(id)
}