package ru.araok.custom

import android.content.Context
import android.net.Uri
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.coroutines.*
import ru.araok.data.dto.MarkDto
import ru.araok.databinding.VideoPlayerMinimalLayoutBinding
import ru.araok.milliSecondsToTimer

class VideoPlayerMinimal
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = VideoPlayerMinimalLayoutBinding.inflate(LayoutInflater.from(context))

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private var delayJob: Job? = null
    private var rangeJob: Job? = null

    private val timerTrackLength: CountDownTimer = object: CountDownTimer(Long.MAX_VALUE, 1000) {
        override fun onTick(p0: Long) {
            binding.seekBar.max = binding.videoView.duration
            binding.seekBar.progress = binding.videoView.currentPosition
            binding.trackLength.text = milliSecondsToTimer(binding.videoView.duration)
            binding.startTrackLength.text = milliSecondsToTimer(binding.videoView.currentPosition)
        }

        override fun onFinish() {

        }
    }

    init {
        addView(binding.root)

        binding.play.setOnClickListener {
            if(binding.videoView.isPlaying)
                binding.videoView.pause()
            else
                binding.videoView.start()
        }

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                binding.videoView.seekTo(p0?.progress!!)
            }

        })
    }

    fun currentPosition() = binding.videoView.currentPosition

    fun duration() = binding.videoView.duration

    fun setVideoPath(path: Uri) {
        binding.videoView.setVideoURI(path)
    }

    fun play() {
        binding.videoView.start()
        timerTrackLength.start()
    }

    fun playRange(mark: MarkDto) {
        rangeJob = scopePlayRange(mark)
        rangeJob?.start()
    }

    private fun scopePlayRange(mark: MarkDto) = scope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        binding.videoView.seekTo(mark.start!!)

        delay(timeMillis = 100)

        if(!binding.videoView.isPlaying)
            binding.videoView.start()

        delayJob = delay(mark.end!!)
        delayJob?.start()
        delayJob?.join()

        binding.videoView.pause()
    }

    private fun delay(end: Int) = scope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        while (binding.videoView.currentPosition <= end)
        ;
    }

    fun stop() {
        delayJob?.cancel()
        rangeJob?.cancel()
        timerTrackLength.cancel()
    }
}