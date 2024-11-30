package com.example.productexplorer.data.local

import com.example.productexplorer.model.Product
import kotlinx.coroutines.flow.Flow

interface IProductLocalDataSource {

    suspend fun saveProducts(product: List<Product>)

    fun getAllProducts(): Flow<List<Product>>

    suspend fun clearProducts()
}