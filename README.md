# Product Explorer App

## Project Overview
Product Explorer is an Android application that fetches and displays products from the Fake Store API, with offline caching capabilities.

## Features
- Fetch products from Fake Store API
- View list of products
- View detailed product information
- Offline caching using Room database
- MVVM Architecture
- Dependency Injection with Hilt

## Technical Stack
- Kotlin
- Jetpack Components (Navigation, ViewModel)
- Retrofit for networking
- Room for local database
- Hilt for dependency injection
- Coroutines and Flow for async programming

## Setup Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run the application

## Screenshots

### Main Screen
![Product List](screenshots/productlist.png)

### Detail Screen
![Product Details](screenshots/product_detail.png)

## Testing
- Unit tests for ViewModel and Repository

## Trade Offs
- Current implementation uses simple error handling
- Implement pagination for product list in case of larger list
- Writing of UI and Instrumentation tests.
- wrote more codes, so as to make system robust so that new features or updates can be added easily.
- Probably add a swiperefreshlistener.
