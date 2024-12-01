package com.example.productexplorer.repo

import com.example.productexplorer.data.local.IProductLocalDataSource
import com.example.productexplorer.data.remote.IProductRemoteDataSource
import com.example.productexplorer.model.Product
import com.example.productexplorer.service.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productLocalDataSource: IProductLocalDataSource,
    private val productRemoteDataSource: IProductRemoteDataSource
): IProductRepository {
    override fun getAllProducts(): Flow<NetworkResult<List<Product>>> = flow{
        emit(NetworkResult.Loading())
        try {
            val products = getRemoteProducts()
            productLocalDataSource.clearProducts()
            saveProducts(products)
            emit(NetworkResult.Success(products))
        }catch (e: HttpException){
            emit(NetworkResult.Error(message = "HTTP Error: ${e.code()}"))
        }catch (e: IOException){
            val localProducts = getSavedProducts().first()
            if (localProducts.isNotEmpty()) {
                emit(NetworkResult.Success(localProducts, isFromCache = true))
            } else {
                emit(NetworkResult.Error(message = "Network Error: ${e.message}"))
            }
        }
    }

    override fun getProduct(id: Int): Flow<NetworkResult<Product>> = flow {
        emit(NetworkResult.Loading())
        try {
            val product = getRemoteProductById(id)
            emit(NetworkResult.Success(product))
        } catch (e: Exception) {
            val localProduct = getSavedProductById(id).first()
            if (localProduct != null) {
                emit(NetworkResult.Success(localProduct, isFromCache = true))
            } else {
                emit(NetworkResult.Error(message = "Error fetching product: ${e.message}"))
            }
        }
    }

    override suspend fun getRemoteProducts(): List<Product> {
        return productRemoteDataSource.getAllRemoteProducts()
    }

    override suspend fun getRemoteProductById(id: Int): Product {
        return productRemoteDataSource.getRemoteProductById(id)
    }

    override suspend fun getSavedProductById(id: Int): Flow<Product> {
        return productLocalDataSource.getProductById(id)
    }

    override suspend fun saveProducts(products: List<Product>) {
        return productLocalDataSource.saveProducts(products)
    }

    override fun getSavedProducts(): Flow<List<Product>> {
        return productLocalDataSource.getAllSavedProducts()
    }
}