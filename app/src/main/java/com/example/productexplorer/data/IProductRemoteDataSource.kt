package com.example.productexplorer.data

import com.example.productexplorer.model.Product

interface IProductRemoteDataSource {

    fun getAllRemoteProducts(): List<Product>
}