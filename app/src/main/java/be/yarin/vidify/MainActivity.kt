package be.yarin.vidify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.yarin.vidapp.VidActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button = findViewById<View>(R.id.button)
        button.setOnClickListener(View.OnClickListener {

//                startActivity(Intent(this@MainActivity, VidActivity::class.java ))
        })
    }
}