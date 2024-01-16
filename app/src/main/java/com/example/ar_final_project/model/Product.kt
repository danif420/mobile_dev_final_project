package com.example.ar_final_project.model

import android.net.Uri

data class Product(
    val id:Int,
    val name:String,
    val price:String,
    val quantity:Int,
    val img:String)

data class UploadProduct(
    val name:String,
    val price:String,
    val quantity:Int,
    val img:Uri?,
    val model_3d:Uri?)