package domain.repository

import MrpDatabase.Product
import kotlinx.coroutines.flow.Flow

interface MrpRepository {

    fun getAllProducts(): Flow<List<Product>>

    fun getAllTopLevelProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product)

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
