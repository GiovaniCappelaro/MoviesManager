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
import com.example.moviesmanager.databinding.ActivityGenreListBinding
import com.example.moviesmanager.model.Genre
import com.example.moviesmanager.model.Model.GENRE_EXTRA
import kotlin.random.Random

class GenreListActivity : AppCompatActivity() {

    private val binding: ActivityGenreListBinding by lazy {
        ActivityGenreListBinding.inflate(layoutInflater)
    }

    private val genreList: MutableList<Genre> = mutableListOf()

    private lateinit var genreAdapter: GenreAdapter

    private lateinit var arl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setTitle(R.string.genres)

        genreAdapter = GenreAdapter(this, genreList)
        binding.genreLv.adapter = genreAdapter

        arl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val genre = result.data?.getParcelableExtra<Genre>(GENRE_EXTRA)

                genre?.let { g ->
                    val pos = genreList.indexOfFirst { it.id == g.id }

                    if (pos == -1) {
                        genreList.add(g)
                    } else {
                        genreList[pos] = g
                    }

                    genreAdapter.notifyDataSetChanged()
                }
            }
        }

        registerForContextMenu(binding.genreLv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.genre_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addGenreMi -> {
                Log.d("MY", "ADD")
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
            .setPositiveButton(R.string.add_genre) { dialog, id ->
                val name = nameEt.text.toString()
                val genre = Genre(Random(System.currentTimeMillis()).nextInt(), name)
                genreList.add(genre)
                genreAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }

    private fun editGenre(position: Int) {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.genre_dialog_box, null)
        val nameEt = view.findViewById<EditText>(R.id.nameEt)
        nameEt.setText(genreList[position].name)

        builder
            .setTitle(R.string.genre_name)
            .setView(view)
            .setPositiveButton(R.string.save_genre) { dialog, id ->
                val name = nameEt.text.toString()
                val genre = Genre(Random(System.currentTimeMillis()).nextInt(), name)
                genreList[position] = genre
                genreAdapter.notifyDataSetChanged()
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }

    private fun removeGenre(position: Int) {
        genreList.removeAt(position)
        genreAdapter.notifyDataSetChanged()
    }
}