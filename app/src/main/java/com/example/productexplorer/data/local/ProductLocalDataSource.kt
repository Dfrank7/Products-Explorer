package com.example.productexplorer.data.local

import com.example.productexplorer.data.db.ProductDao
import com.example.productexplorer.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    private val dao: ProductDao
): IProductLocalDataSource {
    override suspend fun saveProducts(product: List<Product>) {
        dao.insertProducts(product)
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts()
    }
}