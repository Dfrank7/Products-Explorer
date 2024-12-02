package com.example.productexplorer

import com.example.productexplorer.data.db.ProductDao
import com.example.productexplorer.data.local.IProductLocalDataSource
import com.example.productexplorer.data.remote.IProductRemoteDataSource
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating
import com.example.productexplorer.repo.ProductRepository
import com.example.productexplorer.service.NetworkResult
import com.example.productexplorer.service.api.IProductService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var mockLocalDataSource: IProductLocalDataSource

    @Mock
    private lateinit var mockRemoteDataSource: IProductRemoteDataSource

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
        productRepository = ProductRepository(mockLocalDataSource, mockRemoteDataSource)
    }

    @Test
    fun `getAllProducts_returns_success_when_network_call_is_successful`() = runTest {
        // Arrange
        `when`(mockRemoteDataSource.getAllRemoteProducts()).thenReturn(testProducts)
        `when`(mockLocalDataSource.getAllSavedProducts()).thenReturn(kotlinx.coroutines.flow.flowOf(testProducts))

        // Act
        val result = productRepository.getAllProducts().first()

        // Assert
        assertTrue(result is NetworkResult.Success)
        assertEquals(testProducts, (result as NetworkResult.Success).data)
        verify(mockLocalDataSource).clearProducts()
        verify(mockLocalDataSource).saveProducts(testProducts)
    }

    @Test
    fun `getProductById_returns_success_when_network_call_is_successful`() = runTest {
        // Arrange
        val productId = 1
        val product = testProducts.first()
        `when`(mockRemoteDataSource.getRemoteProductById(productId)).thenReturn(product)

        // Act
        val result = productRepository.getProduct(productId).first()

        // Assert
        assertTrue(result is NetworkResult.Success)
        assertEquals(product, (result as NetworkResult.Success).data)
    }
}