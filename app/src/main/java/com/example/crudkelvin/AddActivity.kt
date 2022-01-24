package com.example.crudkelvin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.crudkelvin.room.Movie
import com.example.crudkelvin.room.MovieDb
import com.example.crudkelvin.room.constant
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy{MovieDb(this)}
    private var movieId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupView()
        setuplistener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            constant.TYPE_CREATE ->{
                btn_edit.visibility = View.GONE
            }
            constant.TYPE_READ ->{
                btn_save.visibility = View.GONE
                btn_edit.visibility = View.GONE
                getMovie()
            }
            constant.TYPE_UPDATE ->{
                btn_save.visibility = View.GONE
                getMovie()
            }
        }
    }

    fun setuplistener(){
        btn_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.moviedao().addMovie(
                    Movie(0, et_title.text.toString(),
                    et_description.text.toString())
                )

                finish()
            }
        }
        btn_edit.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.moviedao().updateMovie(
                    Movie(movieId, et_title.text.toString(),
                        et_description.text.toString())
                )

                finish()
            }
        }
    }

    fun getMovie() {
        movieId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.moviedao().getMovie(movieId)[0]
            et_title.setText(movies.title)
            et_description.setText(movies.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
        return super.onSupportNavigateUp()
    }

}