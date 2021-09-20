package crabster.rudakov.sberschoollesson8hwk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val speedometerView = findViewById<SpeedometerView>(R.id.speedometer_view)
        findViewById<SeekBar>(R.id.seekbar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, speed: Int, p2: Boolean) {
                speedometerView.setSpeed(speed)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}