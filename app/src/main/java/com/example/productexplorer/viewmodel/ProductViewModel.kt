package com.example.productexplorer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productexplorer.model.Product
import com.example.productexplorer.repo.IProductRepository
import com.example.productexplorer.service.NetworkResult
import com.example.productexplorer.utility.network.INetworkStatus
import com.example.productexplorer.utility.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: IProductRepository,
    private val networkStatus: INetworkStatus
): ViewModel() {

    private val _productsFlow = MutableStateFlow<NetworkResult<List<Product>>>(NetworkResult.Loading())
    val productsFlow: StateFlow<NetworkResult<List<Product>>> = _productsFlow.asStateFlow()

    private val _productDetailFlow = MutableStateFlow<NetworkResult<Product>>(NetworkResult.Loading())
    val productDetailFlow: StateFlow<NetworkResult<Product>> = _productDetailFlow.asStateFlow()

    private val _checkInternet = MutableStateFlow(false)
    val checkInternet : StateFlow<Boolean> = _checkInternet

    init {
        fetchSavedProducts()
       // fetchProducts()
        checkInternet()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            if (networkStatus.isConnected()) {
                repository.getAllProducts().first()
            }
        }
    }

    private fun checkInternet(){
        viewModelScope.launch {
            //update
            _checkInternet.update {networkStatus.isConnected()}
        }
    }

    private fun fetchSavedProducts(){
        viewModelScope.launch {
            repository.getAllSavedProducts().collect { result ->
                _productsFlow.update {result}
            }
        }
    }

    fun fetchProductById(id: Int) {
        viewModelScope.launch {
            repository.getProduct(id).collect { result ->
                _productDetailFlow.update { result }
            }
        }
    }

}