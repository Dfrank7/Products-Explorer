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

    override fun getAllSavedProducts(): Flow<List<Product>> {
        return dao.getAllSavedProducts()
    }

    override fun getProductById(id: Int): Flow<Product> {
        return dao.getProductById(id)
    }

    override suspend fun clearProducts() {
        dao.clearProducts()
    }
}