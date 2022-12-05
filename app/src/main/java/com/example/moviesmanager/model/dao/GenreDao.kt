package com.example.moviesmanager.model.dao

import com.example.moviesmanager.model.entity.Genre

interface GenreDao {

    fun create(genre: Genre): Int

    fun retrieve(id: Int): Genre?

    fun retrieveAll(): MutableList<Genre>

    fun update(genre: Genre): Int

    fun delete(id: Int): Int
}