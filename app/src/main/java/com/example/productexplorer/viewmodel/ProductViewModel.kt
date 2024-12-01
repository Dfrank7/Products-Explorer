package com.example.productexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productexplorer.model.Product
import com.example.productexplorer.repo.IProductRepository
import com.example.productexplorer.service.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: IProductRepository
): ViewModel() {

    private val _productsFlow = MutableStateFlow<NetworkResult<List<Product>>>(NetworkResult.Loading())
    val productsFlow: StateFlow<NetworkResult<List<Product>>> = _productsFlow.asStateFlow()

    private val _productDetailFlow = MutableStateFlow<NetworkResult<Product>>(NetworkResult.Loading())
    val productDetailFlow: StateFlow<NetworkResult<Product>> = _productDetailFlow.asStateFlow()

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            repository.getAllProducts().collect { result ->
                _productsFlow.value = result
            }
        }
    }

    fun fetchProductById(id: Int) {
        viewModelScope.launch {
            repository.getProduct(id).collect { result ->
                _productDetailFlow.value = result
            }
        }
    }

}