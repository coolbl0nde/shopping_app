package com.example.shoppingapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingapp.data.Product
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

    private val _products = MutableLiveData<List<Product>>()
    val products: MutableLiveData<List<Product>> = _products

    private val _filterProducts = MutableLiveData<List<Product>?>()
    val filterProducts: MutableLiveData<List<Product>?> = _filterProducts

    init {
        loadCategoriesFromFirebase()
        loadProductsFromFirebase()
    }

    private fun loadProductsFromFirebase() {
        viewModelScope.launch {
            db.collection("products").get()
                .addOnSuccessListener { result ->
                    val productList = mutableListOf<Product>()
                    for (document in result) {
                        val product = document.toObject(Product::class.java)
                        productList.add(product)
                    }
                    _products.value = productList
                }.addOnFailureListener {e ->
                    Log.e("HomeViewModel", "Error loading products", e)
                }
        }
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

    fun filterProductsByCategory(category: String) {
        if (category == "All"){
            _filterProducts.value = _products.value
            return
        }

        viewModelScope.launch {
            try {
                val categoryDocument = db.collection("categories")
                    .whereEqualTo("name", category)
                    .get()
                    .await()

                val categoryId = categoryDocument.documents.firstOrNull()?.id

                if (categoryId != null) {
                    val filteredList = _products.value?.filter { it.categoryId == categoryId }
                    _filterProducts.value = filteredList
                }
            }  catch (e: Exception) {
                Log.e("HomeViewModel", "Error filtering products by category", e)
            }
        }
    }

}