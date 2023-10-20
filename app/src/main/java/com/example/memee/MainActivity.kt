package com.example.memee

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private var currentImgUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme() {
        var pBar = findViewById<ProgressBar>(R.id.pBar)//progress bar
        pBar.isVisible = true
//        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"

        // Request a string response from the provided URL.
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImgUrl = response.getString("url")
                var memeImg = findViewById<ImageView>(R.id.memeImgView)
                Glide.with(this).load(currentImgUrl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pBar.isVisible = false
                        return false
                    }

                }).into(memeImg)
                // Display the first 500 characters of the response string.
//                Log.d("Request Success", response.substring(0,500))
            },
            {

//                Log.d("Error! Oops somethings wrong! Error 6969", it.localizedMessage)
            })
// Add the request to the RequestQueue.
//        queue.add(jsonRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest)
    }


    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Checkout this meme $currentImgUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using")
        startActivity(chooser)
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}

