package com.example.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesmanager.R
import com.example.moviesmanager.adapter.GenreAdapter
import com.example.moviesmanager.databinding.ActivityGenreListBinding
import com.example.moviesmanager.model.Genre
import com.example.moviesmanager.model.Model
import com.example.moviesmanager.model.Model.GENRE_EXTRA

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

        binding.genreLv.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewGenre(position)
            }
        }
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

    // TODO persistence

    fun addGenre() {
        val intent = Intent(this, MovieActivity::class.java)
        // arl.launch(intent)
    }

    fun editGenre(position: Int) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra(Model.MOVIE_EXTRA, genreList[position])
        intent.putExtra(Model.MOVIE_EDIT, true)
        // arl.launch(intent)
    }

    fun removeGenre(position: Int) {
        genreList.removeAt(position)
        genreAdapter.notifyDataSetChanged()
    }

    fun viewGenre(position: Int) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra(Model.MOVIE_EXTRA, genreList[position])
        // arl.launch(intent)
    }
}