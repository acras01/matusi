package ua.od.acros.matusi.presentation.misc

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

fun generateId(length: Int= 20): String {
    val alphaNumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return alphaNumeric.shuffled().take(length).joinToString("")
}

fun screenValue(activity: Activity): Pair<Int, Int> {
    val height: Int
    val width: Int

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        width = activity.resources.displayMetrics.widthPixels
        height = activity.resources.displayMetrics.heightPixels
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
    }
    return height to width
}

fun showSnackbar(view: View, @StringRes errorMessageRes: Int) {
    Snackbar.make(view, errorMessageRes, Snackbar.LENGTH_LONG).show()
}

fun showSnackbar(view: View, errorMessage: String) {
    Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
}

fun getWeekDayNameLocalized(day: Int): String = DayOfWeek.of(day).getDisplayName(TextStyle.SHORT, Locale.getDefault())
