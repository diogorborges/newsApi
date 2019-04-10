package com.x0.newsapi.common

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.changeVisibility(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun View.getString(id: Int): String = context.getString(id)

fun View.getString(id: Int, vararg formatArgs: Any): String = context.getString(id, *formatArgs)

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)

fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            val difference = SystemClock.elapsedRealtime() - lastClickTime
            when {
                difference < debounceTime -> return
                else -> action()
            }
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}
