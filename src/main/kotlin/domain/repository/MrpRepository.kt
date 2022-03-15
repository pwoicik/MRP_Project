package domain.repository

import data.entity.ProductEntity
import domain.model.Product
import domain.model.ProductWithComponents
import kotlinx.coroutines.flow.Flow

interface MrpRepository {

    suspend fun deleteProduct(productId: Long)

    fun getAllTopLevelProducts(): Flow<List<Product>>

    fun getAllProductsWithComponents(): Flow<List<ProductWithComponents>>

    suspend fun getProduct(productId: Long): ProductEntity

    fun getProductWithComponents(productId: Long): Flow<ProductWithComponents?>

    suspend fun insertProduct(product: ProductEntity)

    suspend fun insertProduct(
        name: String,
        leadTime: Long,
        batchSize: Long,
        inStock: Long,
        bom: Long = 0,
        parent: Long? = null,
        id: Long? = null
    )
}
