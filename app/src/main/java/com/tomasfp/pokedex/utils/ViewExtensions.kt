package com.tomasfp.pokedex.utils

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.visible() { visibility = View.VISIBLE }

fun View.gone() { visibility = View.GONE }

fun Fragment.showToast(message: String) {
    Toast.makeText(context, message,Toast.LENGTH_LONG).show()
}