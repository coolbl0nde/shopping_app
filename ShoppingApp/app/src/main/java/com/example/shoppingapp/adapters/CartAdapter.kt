package com.example.shoppingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.data.CartProduct
import com.example.shoppingapp.databinding.CartProductItemBinding

class CartAdapter() : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: CartProduct) {
            binding.apply {
                Glide.with(itemView)
                    .load(cart.product.imageUrl)
                    .into(cartImageView)
                cartNameProduct.text = cart.product.name
                cartPriceProduct.text = "${cart.product.price} $"
                quantityTextView.text = "${cart.quantity}"
            }

        }

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =
            CartProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = differ.currentList[position]
        holder.bind(cart)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cart)
        }

        holder.binding.minusCartButton.setOnClickListener {
            onMinusClick?.invoke(cart)
        }

        holder.binding.plusCartButton.setOnClickListener {
            onPlusClick?.invoke(cart)
        }
    }

    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null

}