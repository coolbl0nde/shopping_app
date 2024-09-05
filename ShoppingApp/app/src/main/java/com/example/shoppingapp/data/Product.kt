package com.example.shoppingapp.data

data class Product(
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String? = null,
    val weight: String,
    val price: Float,
    val imageUrl: String = ""
){
    constructor() : this("", "", "", null, "", 0f, "")
}
