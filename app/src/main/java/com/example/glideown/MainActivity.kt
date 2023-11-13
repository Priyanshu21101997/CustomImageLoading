package com.example.glideown

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import java.io.InputStream
import java.net.URL

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var thread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://w7.pngwing.com/pngs/169/918/png-transparent-eren-yeager-armin-arlert-attack-on-titan-levi-mikasa-ackerman-others-black-hair-manga-fictional-character-thumbnail.png"

        val imageView: ImageView = findViewById(R.id.imageView)
        val progressBar: ProgressBar = findViewById(R.id.progress_circular)

        loadImage(url, onSuccess = {
            Log.d(TAG, "Loading Image ${Thread.currentThread().name}")
            imageView.setImageBitmap(it)
            progressBar.visibility = View.GONE
        }, onFailure = {
            Log.d(TAG, "Error fetching Bitmap")
            progressBar.visibility = View.GONE
        })



    }

    private fun loadImage(url: String, onSuccess : (Bitmap)->Unit, onFailure : (String) -> Unit){


        val progressBar: ProgressBar = findViewById(R.id.progress_circular)

        progressBar.visibility = View.VISIBLE


        thread = Thread {
            Log.d(TAG, "Inside thread ${Thread.currentThread().name}")

            if(thread.isInterrupted){
                return@Thread
            }
            val connection = URL(url).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val bitMap = BitmapFactory.decodeStream(inputStream)

            if(bitMap != null){
                handler.post{
                    onSuccess.invoke(bitMap)
                }
            }
            else{
                onFailure.invoke("Bitamp Fetching Failed")
            }

        }

        thread.start();
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        if(::thread.isInitialized)
            thread.interrupt()


    }
}