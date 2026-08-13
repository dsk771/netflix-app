package com.example.netflixclone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_VIDEO_URL = "video_url"
        private const val EXTRA_TITLE = "title"

        fun start(context: Context, videoUrl: String, title: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(EXTRA_VIDEO_URL, videoUrl)
            intent.putExtra(EXTRA_TITLE, title)
            context.startActivity(intent)
        }
    }

    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: return
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Lecture"
        supportActionBar?.title = title

        val playerView = findViewById<PlayerView>(R.id.playerView)
        val webView = findViewById<WebView>(R.id.webView)

        if (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4")) {
            // Utiliser ExoPlayer
            webView.visibility = android.view.View.GONE
            playerView.visibility = android.view.View.VISIBLE
            exoPlayer = ExoPlayer.Builder(this).build().apply {
                playerView.player = this
                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
        } else {
            // Utiliser WebView pour les embeds
            playerView.visibility = android.view.View.GONE
            webView.visibility = android.view.View.VISIBLE
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = WebChromeClient()
            webView.loadUrl(videoUrl)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
        exoPlayer = null
    }
}
