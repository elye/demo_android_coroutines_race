package com.elyeproj.democoroutinesrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.elyeproj.democoroutinesrace.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private var raceEnd = false
    private var greenJob: Job? = null
    private var redJob: Job? = null
    private var blueJob: Job? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            startUpdate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resetRun()
    }

    private fun startUpdate() {
        resetRun()

        greenJob = GlobalScope.launch() {
            startRunning(binding.progressBarGreen)
        }

        redJob = GlobalScope.launch() {
            startRunning(binding.progressBarRed)
        }

        blueJob =GlobalScope.launch() {
            startRunning(binding.progressBarBlue)
        }
    }

    private suspend fun startRunning(progressBar: RoundCornerProgressBar) {
        runOnUiThread {
            progressBar.progress = 0f
        }
        while (progressBar.progress < 1000 && !raceEnd) {
            runOnUiThread {
                progressBar.progress += (1..10).random()
            }
            delay(10)
        }
        if (!raceEnd) {
            raceEnd = true
            runOnUiThread {
                Toast.makeText(this, "${progressBar.tooltipText} won!", Toast.LENGTH_SHORT).show()
            }
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
