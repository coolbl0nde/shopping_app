package com.example.shoppingapp.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentProductDetailsBinding
import com.example.shoppingapp.util.hideBottomNavigationView

class ProductDetailsFragment: Fragment(R.layout.fragment_product_details) {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()

        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.cancelImageView.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            Glide.with(this@ProductDetailsFragment)
                .load(product.imageUrl)
                .into(productDetailsImageView)

            productNameTextView.text = product.name
            productPriceTextView.text = "${product.price} $"
            productDescriptionTextView.text = product.description
        }
    }

}