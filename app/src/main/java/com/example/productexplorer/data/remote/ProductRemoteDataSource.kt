package com.example.productexplorer.data.remote

import com.example.productexplorer.data.remote.IProductRemoteDataSource
import com.example.productexplorer.model.Product
import com.example.productexplorer.service.api.IProductService
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val iProductService: IProductService
): IProductRemoteDataSource {
    override suspend fun getAllRemoteProducts(): List<Product> {
        return iProductService.getAllProducts()
    }

}