package com.example.shoppingapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String? = null,
    val weight: String,
    val price: Float,
    val imageUrl: String = ""
): Parcelable{
    constructor() : this("", "", "", null, "", 0f, "")
}
