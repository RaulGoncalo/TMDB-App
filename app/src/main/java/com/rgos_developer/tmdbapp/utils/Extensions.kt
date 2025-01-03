package com.rgos_developer.tmdbapp.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Fragment.showMessage(message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}