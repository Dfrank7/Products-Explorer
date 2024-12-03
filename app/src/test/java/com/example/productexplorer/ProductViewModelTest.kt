package com.example.productexplorer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.productexplorer.model.Product
import com.example.productexplorer.model.Rating
import com.example.productexplorer.repo.ProductRepository
import com.example.productexplorer.service.NetworkResult
import com.example.productexplorer.utility.network.INetworkStatus
import com.example.productexplorer.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: ProductRepository

    @Mock
    private lateinit var networkStatus: INetworkStatus

    private lateinit var viewModel: ProductViewModel

    private val testDispatcher = StandardTestDispatcher()

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
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchProducts updates productsFlow with success result`() = runTest {
        // Arrange
        `when`(mockRepository.getAllSavedProducts()).thenReturn(
            flowOf(NetworkResult.Success(testProducts))
        )

        // Act
        viewModel = ProductViewModel(mockRepository, networkStatus)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val result = viewModel.productsFlow.value
        assertTrue(result is NetworkResult.Success)
        assertEquals(testProducts, (result as NetworkResult.Success).data)
    }

    @Test
    fun `fetchProductById updates productDetailFlow with success result`() = runTest {
        // Arrange
        val productId = 1
        val product = testProducts.first()
        `when`(mockRepository.getProduct(productId)).thenReturn(
            flowOf(NetworkResult.Success(product))
        )

        `when`(mockRepository.getAllSavedProducts()).thenReturn(
            flowOf(NetworkResult.Success(emptyList())) // Mock an empty list
        )

        // Act
        viewModel = ProductViewModel(mockRepository, networkStatus)
        viewModel.fetchProductById(productId)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val result = viewModel.productDetailFlow.value
        assertTrue(result is NetworkResult.Success)
        assertEquals(product, (result as NetworkResult.Success).data)
    }

    @Test
    fun `fetchProducts updates productsFlow with error result`() = runTest {
        // Arrange
        `when`(mockRepository.getAllSavedProducts()).thenReturn(
            flowOf(NetworkResult.Error("Test Error"))
        )

        // Act
        viewModel = ProductViewModel(mockRepository, networkStatus)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val result = viewModel.productsFlow.value
        assertTrue(result is NetworkResult.Error)
        assertEquals("Test Error", (result as NetworkResult.Error).message)
    }
}