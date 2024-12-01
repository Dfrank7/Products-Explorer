package com.example.productexplorer.service.api

import com.example.productexplorer.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface IProductService {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Product

}