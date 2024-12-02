package com.example.productexplorer.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.productexplorer.databinding.ProductItemBinding
import com.example.productexplorer.model.Product
import com.example.productexplorer.utility.loadPicture

class ProductListAdapter(private val clickListener: ProductListener): ListAdapter<Product, RecyclerView.ViewHolder>(
    ResultDiffCallback()
) {

    class ProductListener(val clickListener: (product: Product)->Unit){
        fun onClick(product: Product) = clickListener(product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder ->{
                val result = getItem(position)
                holder.bind(result, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ResultDiffCallback: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder private constructor(private val binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(result: Product, clickListener: ProductListener){
            binding.apply {
                loadPicture(root.context, result.image, productIv)
                titleTv.text = result.title
                priceTv.text = "$${result.price}"
                view.setOnClickListener {
                    clickListener.onClick(result)
                }
            }
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}