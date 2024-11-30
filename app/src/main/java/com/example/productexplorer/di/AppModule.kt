package com.example.productexplorer.di

import android.content.Context
import androidx.room.Room
import com.example.productexplorer.data.db.ProductDao
import com.example.productexplorer.data.db.ProductDatabase
import com.example.productexplorer.data.local.IProductLocalDataSource
import com.example.productexplorer.data.local.ProductLocalDataSource
import com.example.productexplorer.data.remote.IProductRemoteDataSource
import com.example.productexplorer.data.remote.ProductRemoteDataSource
import com.example.productexplorer.repo.IProductRepository
import com.example.productexplorer.repo.ProductRepository
import com.example.productexplorer.service.api.IProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://fakestoreapi.com/";

    @Provides
    @Singleton
    fun createLoggingInterceptor(): Interceptor{
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun createOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(80, TimeUnit.SECONDS)
            .connectTimeout(80, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun createRetrofitClient(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun createProductService(retrofit: Retrofit): IProductService{
        return retrofit.create(IProductService::class.java)
    }

    @Provides
    @Singleton
    fun createProductDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "product_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun createProductDao(database: ProductDatabase) = database.productDao()

    @Provides
    @Singleton
    fun createProductRemoteDataSource(productService: IProductService): IProductRemoteDataSource{
        return ProductRemoteDataSource(productService)
    }

    @Provides
    @Singleton
    fun createProductLocalDataSource(dao: ProductDao): IProductLocalDataSource{
        return ProductLocalDataSource(dao)
    }

    @Provides
    @Singleton
    fun createProductRepository(local: IProductLocalDataSource, remote: IProductRemoteDataSource): IProductRepository{
        return ProductRepository(local, remote)
    }

}