package crabster.rudakov.sberschoollesson8hwk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val speedometerView = findViewById<SpeedometerView>(R.id.speedometer_view)
        speedometerView.moveSpeedometerView()

        val speedImageView = findViewById<ImageView>(R.id.speed_image_view)
        speedometerView.expandSpeedImageView(speedImageView)

        val speedTextView = findViewById<TextView>(R.id.speed_text_view)
        speedometerView.setTextColorSpeedTextView(speedTextView)
    }

}