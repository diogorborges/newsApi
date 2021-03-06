package com.x0.newsapi.common

import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import com.x0.newsapi.R

fun setThumbnailImage(imageView: ImageView, path: String?) {
    GlideApp.with(imageView)
        .load(path)
        .apply(RequestOptions().circleCrop())
        .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
        .into(imageView)
}
