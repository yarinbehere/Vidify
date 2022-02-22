package be.yarin.vidify.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.yarin.vidify.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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