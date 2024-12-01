package com.example.productexplorer.data.remote

import com.example.productexplorer.model.Product

interface IProductRemoteDataSource {

    suspend fun getAllRemoteProducts(): List<Product>

    suspend fun getRemoteProductById(id: Int): Product
}