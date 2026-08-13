package com.example.netflixclone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_SLUG = "slug"

        fun start(context: Context, slug: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_SLUG, slug)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val slug = intent.getStringExtra(EXTRA_SLUG) ?: return
        val dbHelper = DatabaseHelper(this)
        dbHelper.copyDatabaseFromAssets(this)
        val movie = dbHelper.getMovieBySlug(slug) ?: return

        val posterView = findViewById<ImageView>(R.id.detailPoster)
        val titleView = findViewById<TextView>(R.id.detailTitle)
        val synopsisView = findViewById<TextView>(R.id.detailSynopsis)
        val playButton = findViewById<Button>(R.id.playButton)

        titleView.text = movie.titre
        synopsisView.text = movie.synopsis

        if (movie.poster != null && movie.poster.isNotEmpty()) {
            Glide.with(this)
                .load("file:///android_asset/posters/${movie.poster}")
                .placeholder(R.drawable.placeholder)
                .into(posterView)
        } else {
            posterView.setImageResource(R.drawable.placeholder)
        }

        playButton.setOnClickListener {
            PlayerActivity.start(this, movie.videoUrl, movie.titre)
        }
    }
}
