package com.example.netflixclone

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "films.db"
        private const val DB_VERSION = 1
        private const val ASSET_DB_PATH = "films.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // La base est déjà prête, on la copie depuis assets
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun copyDatabaseFromAssets(context: Context) {
        val dbFile = context.getDatabasePath(DB_NAME)
        if (dbFile.exists()) return
        dbFile.parentFile?.mkdirs()
        context.assets.open(ASSET_DB_PATH).use { input ->
            FileOutputStream(dbFile).use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getAllMovies(): List<Movie> {
        val movies = mutableListOf<Movie>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT slug, titre, synopsis, poster, video_url FROM films ORDER BY titre", null)
        while (cursor.moveToNext()) {
            movies.add(
                Movie(
                    slug = cursor.getString(0),
                    titre = cursor.getString(1),
                    synopsis = cursor.getString(2),
                    poster = cursor.getString(3),
                    videoUrl = cursor.getString(4)
                )
            )
        }
        cursor.close()
        db.close()
        return movies
    }

    fun getMovieBySlug(slug: String): Movie? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT slug, titre, synopsis, poster, video_url FROM films WHERE slug = ?", arrayOf(slug))
        var movie: Movie? = null
        if (cursor.moveToFirst()) {
            movie = Movie(
                slug = cursor.getString(0),
                titre = cursor.getString(1),
                synopsis = cursor.getString(2),
                poster = cursor.getString(3),
                videoUrl = cursor.getString(4)
            )
        }
        cursor.close()
        db.close()
        return movie
    }
}
