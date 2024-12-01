package com.example.productexplorer.data.local

import com.example.productexplorer.model.Product
import kotlinx.coroutines.flow.Flow

interface IProductLocalDataSource {

    suspend fun saveProducts(product: List<Product>)

    fun getAllSavedProducts(): Flow<List<Product>>

    fun getProductById(id: Int): Flow<Product>

    suspend fun clearProducts()
}