package ru.araok

import android.content.Context
import android.os.Looper
import android.widget.Toast
import java.util.*
import java.util.logging.Handler

fun milliSecondsToTimer(milliseconds: Int): String {
    var minutesString = ""
    var secondsString = ""

    var minutes = ((milliseconds % (1000 * 60 * 60)) / (1000 * 60))
    var seconds = (((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000))

    minutesString = if(minutes < 10) {
        "0$minutes"
    } else {
        "$minutes"
    }

    secondsString = if(seconds < 10) {
        "0$seconds"
    } else {
        "$seconds"
    }

    return "$minutesString:$secondsString"
}

fun timerToMilliSeconds(timer: String): Int {
    val minutes = timer.split(":")[0].toInt()
    val seconds = timer.split(":")[1].toInt()

    return minutes * 60 * 1000 + seconds * 1000
}

fun backgroundThreadShortToast(context: Context, msg: String) {
    if(Objects.nonNull(context) && Objects.nonNull(msg)) {
        android.os.Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

fun maskPhoneToNumberPhone(maskPhone: String) = maskPhone
    .replace("+7", "")
    .replace("(", "")
    .replace(")", "")
    .replace("-", "")