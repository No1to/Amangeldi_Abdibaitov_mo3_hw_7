package com.example.myapplication

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private lateinit var play: ImageButton
    private lateinit var elapsed: TextView
    private lateinit var remaining: TextView
    private lateinit var position: SeekBar
    private lateinit var volume: SeekBar
    private var totalTime = 0

    private lateinit var drawerToggle: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupMediaPlayer()
        setupSeekBarUpdate()
        setupNavigationMenu()
    }

    private fun initViews() {
        play = findViewById(R.id.imgB_play)
        elapsed = findViewById(R.id.elapsed)
        remaining = findViewById(R.id.remaining)
        position = findViewById(R.id.sb_time)
        volume = findViewById(R.id.volume)
        drawerToggle = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this, drawerToggle, R.string.open, R.string.close)
    }

    private fun setupMediaPlayer() {
        play.setOnClickListener {
            if (mp.isPlaying) {
                mp.pause()
                play.setImageResource(R.drawable.play)
            } else {
                mp.start()
                play.setImageResource(R.drawable.pause)
            }
        }
        mp = MediaPlayer.create(this, R.raw.music)
        // Дополнительные настройки медиаплеера
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration
        position.max = totalTime


        Music.setSeekBar(volume, mp, true)
        Music.setSeekBar(position, mp, null, true)
    }

    private fun setupSeekBarUpdate() {
        val handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            @SuppressLint("SetTextI18n")
            override fun handleMessage(msg: Message) {
                val currentPosition = msg.what
                position.progress = currentPosition
                elapsed.text = Music.createTimeLabel(currentPosition)
                remaining.text = "- ${Music.createTimeLabel(totalTime - currentPosition)}"
            }
        }

        Thread {
            while (true) {
                try {
                    val msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    Log.e("Thread", e.message.toString())
                }
            }
        }.start()
    }

    private fun setupNavigationMenu() {
        drawerToggle.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> Toast.makeText(this, "Clicked Home", Toast.LENGTH_SHORT).show()
                R.id.fileMusic -> Toast.makeText(this, "Clicked File", Toast.LENGTH_SHORT).show()
                R.id.sync -> Toast.makeText(this, "Clicked Sync", Toast.LENGTH_SHORT).show()
                R.id.trash -> Toast.makeText(this, "Clicked Trash", Toast.LENGTH_SHORT).show()
                R.id.settings -> Toast.makeText(this, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.login -> Toast.makeText(this, "Clicked Login", Toast.LENGTH_SHORT).show()
                R.id.share -> Toast.makeText(this, "Clicked Share", Toast.LENGTH_SHORT).show()
                R.id.rateUs -> Toast.makeText(this, "Clicked Rate Us", Toast.LENGTH_SHORT).show()

            }
            true
        }
    }
}
