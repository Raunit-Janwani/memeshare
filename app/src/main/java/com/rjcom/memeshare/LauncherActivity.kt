package com.rjcom.memeshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView



class LauncherActivity : AppCompatActivity() {
      var button:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher2)
        loadImage()

    }
   fun loadImage(){
       button=findViewById(R.id.button)
       button?.setOnClickListener({
           loadPage()
       })
       var backgroundImage : ImageView= findViewById<ImageView>(R.id.backgroundImage)
       backgroundImage.setImageResource(R.drawable.reddit)
   }
     fun loadPage(){

        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}


