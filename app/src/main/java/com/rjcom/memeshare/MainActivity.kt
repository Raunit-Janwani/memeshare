package com.rjcom.memeshare

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var img :ImageView

     var shareButton:Button?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Instantiate the RequestQueue.
        val view=findViewById<ImageView>(R.id.memeImage)
        loadMeme(view)
    }

    fun loadMeme(view : View) {
        shareButton=findViewById(R.id.shareButton)
        shareButton?.isVisible=false
        var connected = false

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connected =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED

        if (connected) {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            val queue = Volley.newRequestQueue(this)
            val url = "https://meme-api.herokuapp.com/gimme"
            shareButton?.isVisible = false
            var memeImage=findViewById<ImageView>(R.id.memeImage)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val imgUrl = response.get("url")

                    Glide.with(this).load(imgUrl).listener(object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            progressBar.visibility = View.GONE
                            shareButton?.isVisible = false

                            Toast.makeText(
                                this@MainActivity,
                                "Sorry Meme couldnt be loaded either API not working Or Internet is OFF or Press NEXT",
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            shareButton?.isVisible = true

                            return false

                        }
                    }
                    ).into(memeImage)



                },
                {
                    Toast.makeText(
                        this,
                        "Sorry some error occurred" ,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
            // Add the request to the RequestQueue.
            queue?.add(jsonObjectRequest)
        } else {
            Toast.makeText(
                this,
                "Please Connect to Internet And Restart The App",
                Toast.LENGTH_LONG
            ).show()
        }


    }



    fun shareMeme(view: View) {
                 img=findViewById(R.id.memeImage)
                 try {
                     val bitmapDrawable = img.drawable as BitmapDrawable
                     val bitmap = bitmapDrawable.bitmap
                     val bitmapPath = MediaStore.Images.Media.insertImage(this.getContentResolver(),
                         bitmap,"IMG_" + java.util.UUID.randomUUID().toString(),null);
                     if (bitmapPath != null) {
                         val bitmapUri = Uri.parse(bitmapPath)
                         val shareIntent = Intent(Intent.ACTION_SEND)
                         shareIntent.type = "image/*"
                         shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                         startActivity(Intent.createChooser(shareIntent, "SHare using app"))
                     }
                     else
                     {
                         Toast.makeText(this, "Error bitmapPath :" +bitmap, Toast.LENGTH_SHORT).show()
                     }
                 }
                 catch (e:Exception){
                     Toast.makeText(this, "error: "+e.localizedMessage, Toast.LENGTH_SHORT).show()

                 }



    }

    }



