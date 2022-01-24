package com.example.crudkelvin

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudkelvin.room.Movie
import com.example.crudkelvin.room.MovieDb
import com.example.crudkelvin.room.constant
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val db by lazy{ MovieDb(this) }
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setuplistener()
        setupRecyclerView()

    }

    override fun onStart(){
        super.onStart()
        loadNote()
    }

    fun loadNote(){
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.moviedao().getMovies()
            Log.d("MainActivity", "dbresponse: $movies")
            withContext(Dispatchers.Main){
                movieAdapter.setData(movies)
            }
        }
    }

    fun setuplistener(){
        add_movie.setOnClickListener {
        intentEdit(0,constant.TYPE_CREATE)
        }
    }

    fun intentEdit(movieId: Int, intentType:Int){
        startActivity(
            Intent(applicationContext, AddActivity::class.java)
                .putExtra("intent_id", movieId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView(){
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdaptelListener{
            override fun onClick(movie: Movie) {
                intentEdit(movie.id,constant.TYPE_READ)
            }

            override fun onUpdate(movie: Movie) {
                intentEdit(movie.id,constant.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
                deleteDialog(movie)
            }

        })
        rv_movie.apply{
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }

    private fun deleteDialog(movie: Movie){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin hapus ${movie.title}?")
            setNegativeButton("batal") { dialoginterface, i ->
                dialoginterface.dismiss()
            }
            setPositiveButton("Hapus") { dialoginterface, i ->
                dialoginterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.moviedao().deleteMovie(movie)
                    loadNote()
                }
            }
        }
        alertDialog.show()
    }
}