package com.example.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.adapter.GenreAdapter
import com.example.moviesmanager.controller.GenreController
import com.example.moviesmanager.databinding.ActivityGenreListBinding
import com.example.moviesmanager.model.entity.Genre
import com.example.moviesmanager.model.entity.Model.GENRE_EXTRA
import kotlin.random.Random

class GenreListActivity : AppCompatActivity() {

    private val binding: ActivityGenreListBinding by lazy {
        ActivityGenreListBinding.inflate(layoutInflater)
    }

    private lateinit var genreList: MutableList<Genre>

    private lateinit var genreAdapter: GenreAdapter

    private lateinit var genreController: GenreController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setTitle(R.string.genres)

        genreController = GenreController(this)

        genreList = genreController.getAll()

        Log.d("MY", genreList.toString())

        genreAdapter = GenreAdapter(this, genreList)
        binding.genreLv.adapter = genreAdapter

        registerForContextMenu(binding.genreLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.genre_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addGenreMi -> {
                addGenre()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.genre_ctx_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position

        return when (item.itemId) {
            R.id.removeGenreMi -> {
                removeGenre(position)
                true
            }
            R.id.editGenreMi -> {
                editGenre(position)
                true
            }
            else -> {
                false
            }
        }
    }

    private fun addGenre() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.genre_dialog_box, null)
        val nameEt = view.findViewById<EditText>(R.id.nameEt)

        builder
            .setTitle(R.string.genre_name)
            .setView(view)
            .setPositiveButton(R.string.add_genre) { _, _ ->
                val name = nameEt.text.toString()
                val genre = Genre(Random(System.currentTimeMillis()).nextInt(), name)
                genre.id = genreController.insert(genre)
                genreList.add(genre)
                genreAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
        builder.create().show()
    }

    private fun editGenre(position: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.genre_dialog_box, null)
        val nameEt = view.findViewById<EditText>(R.id.nameEt)
        val genre = genreList[position]
        nameEt.setText(genre.name)

        builder
            .setTitle(R.string.genre_name)
            .setView(view)
            .setPositiveButton(R.string.save_genre) { _, _ ->
                val name = nameEt.text.toString()
                genre.name = name
                genreController.edit(genre)
                genreList[position] = genre
                genreAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }
        builder.create().show()
    }

    private fun removeGenre(position: Int) {
        val id = genreList[position].id
        genreController.remove(id)
        genreList.removeAt(position)
        genreAdapter.notifyDataSetChanged()
    }
}