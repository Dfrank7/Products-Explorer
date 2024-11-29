package com.example.productexplorer.service.api

import com.example.productexplorer.model.Product
import retrofit2.http.GET

interface IProductService {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

}