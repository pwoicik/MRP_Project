package domain.repository

import data.entity.ProductEntity
import domain.model.Component
import kotlinx.coroutines.flow.Flow

interface MrpRepository {

    suspend fun deleteProduct(productId: Long)

    fun getAllProducts(): Flow<List<ProductEntity>>

    fun getProduct(productId: Long): Flow<ProductEntity>

    suspend fun insertProduct(name: String, leadTime: Long, inStock: Long, components: List<Component> = emptyList())

    suspend fun updateProduct(productTree: ProductEntity)
}
