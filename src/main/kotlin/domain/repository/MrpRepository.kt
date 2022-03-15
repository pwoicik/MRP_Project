package domain.repository

import data.entity.ProductTree
import domain.model.MutableProductTreeNode
import kotlinx.coroutines.flow.Flow

interface MrpRepository {

    suspend fun deleteProduct(productId: Long)

    fun getAllProducts(): Flow<List<ProductTree>>

    fun getProduct(productId: Long): Flow<ProductTree>

    suspend fun insertProduct(product: MutableProductTreeNode)

    suspend fun updateProduct(productTree: ProductTree)
}
