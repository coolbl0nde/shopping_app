package com.example.shoppingapp.util

import android.os.Message

sealed class RegisterValidation {
    object Success: RegisterValidation()
    data class Failed(val message: String): RegisterValidation()
}

data class RegisterFieldsState(
    val email: RegisterValidation,
    val password: RegisterValidation
)