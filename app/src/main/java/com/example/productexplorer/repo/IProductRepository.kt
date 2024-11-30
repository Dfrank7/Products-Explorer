package com.example.productexplorer.repo

import com.example.productexplorer.model.Product
import com.example.productexplorer.service.NetworkResult
import kotlinx.coroutines.flow.Flow

interface IProductRepository {

    fun getAllProducts(): Flow<NetworkResult<List<Product>>>

    suspend fun getRemoteProducts(): List<Product>

    suspend fun saveProducts(products: List<Product>)

    fun getSavedProducts(): Flow<List<Product>>

}