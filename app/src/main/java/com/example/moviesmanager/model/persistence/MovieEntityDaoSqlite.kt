package com.example.moviesmanager.model.persistence

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.moviesmanager.model.dao.MovieEntityDao
import com.example.moviesmanager.model.entity.Movie
import com.example.moviesmanager.model.entity.MovieEntity
import java.sql.SQLException

class MovieEntityDaoSqlite(context: Context): MovieEntityDao {

    companion object Constants {
        private const val DATABASE_FILE = "movies"
        private const val TABLE_NAME = "movie"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_STUDIO = "studio"
        private const val COLUMN_PRODUCER = "producer"
        private const val COLUMN_DURATION = "duration"
        private const val COLUMN_WATCHED = "watched"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_GENRE_ID = "genre_id"

        private const val CREATE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                    "$COLUMN_ID       INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME     TEXT    NOT NULL, " +
                    "$COLUMN_YEAR     TEXT    NOT NULL, " +
                    "$COLUMN_STUDIO   TEXT            , " +
                    "$COLUMN_PRODUCER TEXT            , " +
                    "$COLUMN_DURATION INTEGER NOT NULL, " +
                    "$COLUMN_WATCHED  INTEGER NOT NULL, " +
                    "$COLUMN_RATING   INTEGER         , " +
                    "$COLUMN_GENRE_ID INTEGER NOT NULL);"
    }

    private val sqliteDatabase: SQLiteDatabase
    init {
        sqliteDatabase = context.openOrCreateDatabase(
            DATABASE_FILE,
            MODE_PRIVATE,
            null
        )

        try {
            sqliteDatabase.execSQL(CREATE_STATEMENT)
        } catch (e: SQLException) {
            Log.e("SQLite", e.toString())
        }
    }

    private fun MovieEntity.toContentValues() = with(ContentValues()) {
        put(COLUMN_ID, id)
        put(COLUMN_NAME, name)
        put(COLUMN_YEAR, year)
        put(COLUMN_STUDIO, studio)
        put(COLUMN_PRODUCER, producer)
        put(COLUMN_DURATION, duration)
        put(COLUMN_WATCHED, watched)
        put(COLUMN_RATING, rating)
        put(COLUMN_GENRE_ID, genreId)
        this
    }

    private fun Cursor.rowToMovie() = MovieEntity(
        getInt(getColumnIndexOrThrow(COLUMN_ID)),
        getString(getColumnIndexOrThrow(COLUMN_NAME)),
        getInt(getColumnIndexOrThrow(COLUMN_YEAR)),
        getString(getColumnIndexOrThrow(COLUMN_STUDIO)),
        getString(getColumnIndexOrThrow(COLUMN_PRODUCER)),
        getLong(getColumnIndexOrThrow(COLUMN_DURATION)),
        getInt(getColumnIndexOrThrow(COLUMN_WATCHED)) != 0,
        getInt(getColumnIndexOrThrow(COLUMN_RATING)),
        getInt(getColumnIndexOrThrow(COLUMN_GENRE_ID))
    )

    override fun create(movie: MovieEntity) = sqliteDatabase.insert(
        TABLE_NAME,
        null,
        movie.toContentValues()
    ).toInt()

    override fun retrieve(id: Int): MovieEntity? {
        val cursor = sqliteDatabase.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        val movie = if (cursor.moveToFirst()) {
            cursor.rowToMovie()
        } else {
            null
        }
        cursor.close()
        return movie
    }

    override fun retrieveAll(): MutableList<MovieEntity> {
        val movieList = mutableListOf<MovieEntity>()
        val cursor = sqliteDatabase.rawQuery(
            "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_NAME",
            null
        )
        while (cursor.moveToNext()) {
            movieList.add(cursor.rowToMovie())
        }
        cursor.close()
        return movieList
    }

    override fun update(movie: MovieEntity) = sqliteDatabase.update(
        TABLE_NAME,
        movie.toContentValues(),
        "$COLUMN_ID = ?",
        arrayOf(movie.id.toString())
    )

    override fun delete(id: Int) = sqliteDatabase.delete(
        TABLE_NAME,
        "$COLUMN_ID = ?",
        arrayOf(id.toString())
    )
}