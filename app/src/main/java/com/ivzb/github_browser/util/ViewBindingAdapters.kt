package com.ivzb.github_browser.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun imageUrl(imageView: ImageView, url: String?) {
    if (url?.isNotEmpty() == true) {
        Glide.with(imageView)
            .load(url)
            .into(imageView)
    }
}
