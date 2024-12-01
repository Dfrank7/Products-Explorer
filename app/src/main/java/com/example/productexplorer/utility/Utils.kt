package com.example.productexplorer.utility

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun loadPicture(context: Context, path: String, imageView: ImageView){
    Glide.with(context).load(path).into(imageView)
}