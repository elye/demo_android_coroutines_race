package com.elyeproj.democoroutinesrace

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private var raceEnd = false
    private var greenJob: Job? = null
    private var redJob: Job? = null
    private var blueJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener {
            startUpdate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resetRun()
    }

    private fun startUpdate() {
        resetRun()

        greenJob = launch(Android) {
            startRunning(progressBarGreen)
        }

        redJob = launch(Android) {
            startRunning(progressBarRed)
        }

        blueJob =launch(Android) {
            startRunning(progressBarBlue)
        }
    }

    private suspend fun startRunning(progressBar: RoundCornerProgressBar) {
        progressBar.progress = 0f
        while (progressBar.progress < 1000 && !raceEnd) {
            delay(10)
            progressBar.progress += (1..10).random()
        }
        if (!raceEnd) {
            raceEnd = true
            Toast.makeText(this, "${progressBar.tooltipText} won!", Toast.LENGTH_SHORT).show()
        }
    }

    fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

    private fun resetRun() {
        raceEnd = false
        greenJob?.cancel()
        blueJob?.cancel()
        redJob?.cancel()
    }
}
