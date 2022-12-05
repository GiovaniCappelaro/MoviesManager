package com.example.moviesmanager.model.persistence

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.moviesmanager.model.dao.GenreDao
import com.example.moviesmanager.model.entity.Genre
import com.example.moviesmanager.model.entity.Movie
import java.sql.SQLException

class GenreDaoSqlite(context: Context): GenreDao {

    companion object Constants {
        private const val DATABASE_FILE = "genres"
        private const val TABLE_NAME = "genre"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"

        private const val CREATE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID   INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT    NOT NULL);"
    }

    private val sqliteDatabase: SQLiteDatabase
    init {
        sqliteDatabase = context.openOrCreateDatabase(
            DATABASE_FILE,
            Context.MODE_PRIVATE,
            null
        )

        try {
            sqliteDatabase.execSQL(CREATE_STATEMENT)
        } catch (e: SQLException) {
            Log.e("SQLite", e.toString())
        }
    }

    private fun Genre.toContentValues() = with(ContentValues()) {
        put(COLUMN_ID, id)
        put(COLUMN_NAME, name)
        this
    }

    private fun Cursor.rowToGenre() = Genre(
        getInt(getColumnIndexOrThrow(COLUMN_ID)),
        getString(getColumnIndexOrThrow(COLUMN_NAME))
    )

    override fun create(genre: Genre) = sqliteDatabase.insert(
        TABLE_NAME,
        null,
        genre.toContentValues()
    ).toInt()

    override fun retrieve(id: Int): Genre? {
        val cursor = sqliteDatabase.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        val genre = if (cursor.moveToFirst()) {
            cursor.rowToGenre()
        } else {
            null
        }
        cursor.close()
        return genre
    }

    override fun retrieveAll(): MutableList<Genre> {
        val genreList = mutableListOf<Genre>()
        val cursor = sqliteDatabase.rawQuery(
            "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME",
            null
        )
        while (cursor.moveToNext()) {
            genreList.add(cursor.rowToGenre())
        }
        cursor.close()
        return genreList
    }

    override fun update(genre: Genre) = sqliteDatabase.update(
        TABLE_NAME,
        genre.toContentValues(),
        "$COLUMN_ID = ?",
        arrayOf(genre.id.toString())
    )

    override fun delete(id: Int) = sqliteDatabase.delete(
        TABLE_NAME,
        "$COLUMN_ID = ?",
        arrayOf(id.toString())
    )
}