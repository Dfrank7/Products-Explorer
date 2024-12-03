package com.example.productexplorer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.productexplorer.data.local.IProductLocalDataSource
import com.example.productexplorer.data.remote.IProductRemoteDataSource
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating
import com.example.productexplorer.repo.ProductRepository
import com.example.productexplorer.service.NetworkResult
import com.example.productexplorer.utility.network.INetworkStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var mockLocalDataSource: IProductLocalDataSource

    @Mock
    private lateinit var mockRemoteDataSource: IProductRemoteDataSource

    @Mock
    private lateinit var networkStatus: INetworkStatus

    private lateinit var productRepository: ProductRepository

    private val testProducts = listOf(
        Product(
            id = 1,
            title = "Test Product",
            price = 9.99,
            description = "Test Description",
            category = "Test Category",
            image = "test_image_url",
            rating = Rating(4.5, 100)
        )
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        productRepository = ProductRepository(mockLocalDataSource, mockRemoteDataSource, networkStatus)
    }

    @Test
    fun `getAllProducts returns success when network call is successful`() = runTest {
        // Arrange
        `when`(mockRemoteDataSource.getAllRemoteProducts()).thenReturn(testProducts)
        `when`(mockLocalDataSource.getAllSavedProducts()).thenReturn(
            kotlinx.coroutines.flow.flowOf(
                testProducts
            )
        )
        `when`(networkStatus.isConnected()).thenReturn(true)

        // Act
        val result = productRepository.getAllProducts().first()

        // Assert
        Assert.assertTrue(result is NetworkResult.Success)
        Assert.assertEquals(testProducts, (result as NetworkResult.Success).data)
        verify(mockLocalDataSource).clearProducts()
        verify(mockLocalDataSource).saveProducts(testProducts)
    }

    @Test
    fun `getProductById_returns_success_when_network_call_is_successful`() = runTest {
        // Arrange
        val productId = 1
        val product = testProducts.first()
        `when`(mockRemoteDataSource.getRemoteProductById(productId)).thenReturn(product)
        `when`(mockLocalDataSource.getProductById(productId))
            .thenReturn(flow { emit(product) })

        // Act
        val result = productRepository.getProduct(productId).last()

        // Assert
        Assert.assertTrue(result is NetworkResult.Success)
        Assert.assertEquals(product, (result as NetworkResult.Success).data)
    }

    @Test
    fun `getAllSavedProducts emits Loading, Success when products are available`() = runTest {
        // Arrange
        val savedProducts = testProducts

        // Mock the getSavedProducts method to return a flow emitting saved products
        `when`(productRepository.getSavedProducts()).thenReturn(flow { emit(savedProducts) })

        // Act
        val results = productRepository.getAllSavedProducts().toList()

        // Assert
        assertEquals(2, results.size)
        assertTrue(results[0] is NetworkResult.Loading)
        assertTrue(results[1] is NetworkResult.Success)
        assertEquals(savedProducts, (results[1] as NetworkResult.Success).data)
        assertTrue((results[1] as NetworkResult.Success).isFromCache) // Ensure isFromCache is true
    }

    @Test
    fun `getProductById emits Loading and Success when network call succeeds`() = runTest {
        // Arrange
        val productId = 1
        val expectedProduct = testProducts.first()
        `when`(mockRemoteDataSource.getRemoteProductById(productId)).thenReturn(expectedProduct)
        `when`(mockLocalDataSource.getAllSavedProducts()).thenReturn(flow { emit(emptyList<Product>()) }) // Returns an empty flow
        `when`(mockLocalDataSource.getProductById(productId))
            .thenReturn(flow { emit(expectedProduct) })

        // Act
        val results = productRepository.getProduct(productId).toList()

        // Assert
        Assert.assertTrue(results[0] is NetworkResult.Loading)
        Assert.assertTrue(results[1] is NetworkResult.Success)
        Assert.assertEquals(expectedProduct, (results[1] as NetworkResult.Success).data)
    }
}