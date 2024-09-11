package com.example.shoppingapp.data

data class CartProduct(
    val product: Product,
    val quantity: Int
){
    constructor(): this(Product(), 1)
}
