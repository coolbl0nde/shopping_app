package com.example.shoppingapp.data

data class Product(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val description: String? = null,
    val material: String? = null,
    val weight: String = "",
    val price: String = "",
    val imageUrl: String = ""
)
