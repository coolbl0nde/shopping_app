package com.example.shoppingapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.adapters.ProductAdapter
import com.example.shoppingapp.databinding.FragmentHomeBinding
import com.example.shoppingapp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        observeCategories()

        setupRecyclerView()
        observeProducts()
    }

    private fun observeProducts() {
        viewModel.products.observe(viewLifecycleOwner){
            productAdapter.updateProducts(it)
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(emptyList())

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun setupSpinner() {
        categoryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item,
            mutableListOf()
        )
        categoryAdapter.setDropDownViewResource(R.layout.spinner_list_items)
        binding.categorySpinner.adapter = categoryAdapter


        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categoryAdapter.getItem(position)
                filterProductsByCategory(selectedCategory.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Ничего не делать, если ничего не выбрано
            }
        }

    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { categoryList ->
                    categoryAdapter.clear()
                    categoryAdapter.addAll(categoryList)
                    categoryAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun filterProductsByCategory(category: String) {
        val selectedCategoryId = viewModel.categories.find {

        }

        val filteredProducts = if (category == "All"){
            viewModel.products
        }else
    }

}