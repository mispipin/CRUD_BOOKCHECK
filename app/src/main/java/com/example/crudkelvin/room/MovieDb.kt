package com.example.crudkelvin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.locks.Lock

@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieDb : RoomDatabase(){
    abstract fun moviedao():MovieDao

    companion object{

        @Volatile private var instance : MovieDb? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: builDatabase(context).also{
                instance = it
            }
        }

        private fun builDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            MovieDb::class.java,
            "movie12345.db"
        ).build()
    }
}