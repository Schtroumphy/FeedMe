package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.BuildConfig
import com.jeanloth.project.android.kotlin.feedme.core.AppPreferences
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.ObserveAllProductsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.SaveProductUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.SyncProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductVM @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val syncProductUseCase: SyncProductUseCase,
    private val saveProductUseCase: SaveProductUseCase,
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
) : ViewModel() {

    private val _products : MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products : StateFlow<List<Product>> = _products.asStateFlow()

    init {
        viewModelScope.launch {
            observeAllProductsUseCase.invoke().collect {
                _products.value = it.onEach {
                    it.imageId = applicationContext.resources.getIdentifier(it.image, "drawable", applicationContext.packageName)
                }
            }
        }
    }

    fun syncProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            if(BuildConfig.DATABASE_VERSION > AppPreferences.databaseVersion){
                Log.d("ProductVM", "Launch product synchronization" )
                syncProductUseCase()
                AppPreferences.databaseVersion = BuildConfig.DATABASE_VERSION
            }
        }
    }

    fun saveProduct(label : String, image: String?){
        viewModelScope.launch(Dispatchers.IO){

            saveProductUseCase.invoke(Product(
                label = label,
                image = image
            ))
        }
    }

    fun removeProduct(product: Product){
        viewModelScope.launch(Dispatchers.IO){
            //removeProductUseCase.invoke(product)
        }
    }
}