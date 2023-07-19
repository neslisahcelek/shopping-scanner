package com.example.shoppingscanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingscanner.databinding.ProductItemBinding
import com.example.shoppingscanner.model.ProductBarcode

class ProductAdapter (private val data: ArrayList<ProductBarcode>):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    lateinit var binding: ProductItemBinding

    class ProductViewHolder (view: ProductItemBinding):RecyclerView.ViewHolder(view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.itemView.apply {
                binding.product = data[position]
            }
    }
}