package com.rgos_developer.tmdbapp.Utils

import android.app.Activity
import android.widget.Toast

fun Activity.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}