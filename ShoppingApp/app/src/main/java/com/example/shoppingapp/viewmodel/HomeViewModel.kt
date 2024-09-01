package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore
): ViewModel() {

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    init {
        loadCategoriesFromFirebase()
    }

    private fun loadCategoriesFromFirebase(){
        viewModelScope.launch {
            try {
                val result = db.collection("categories").get().await()
                val categoryList = mutableListOf<String>()
                categoryList.add("All")
                for (document in result) {
                    val categoryName = document.getString("name") ?: ""
                    categoryList.add(categoryName)
                }
                _categories.value = categoryList
            }catch (e: Exception){
                Log.e("HomeViewModel", "Error loading categories", e)
            }
        }
    }

}