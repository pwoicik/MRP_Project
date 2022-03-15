package data.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import data.MrpDatabase
import data.entity.ProductTree
import domain.model.MutableProductTreeNode
import domain.repository.MrpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MrpRepositoryImpl(
    private val db: MrpDatabase,
    private val dbCoroutineDispatcher: CoroutineContext,
) : MrpRepository {

    override suspend fun deleteProduct(productId: Long) = withContext(dbCoroutineDispatcher) {
        db.productQueries.deleteProduct(productId)
    }

    override fun getAllProducts(): Flow<List<ProductTree>> {
        return db.productQueries.getAllProducts().asFlow().mapToList(dbCoroutineDispatcher)
    }

    override fun getProduct(productId: Long): Flow<ProductTree> {
        return db.productQueries.getProduct(productId).asFlow().mapToOne(dbCoroutineDispatcher)
    }

    override suspend fun insertProduct(product: MutableProductTreeNode) = withContext(dbCoroutineDispatcher) {
        db.productQueries.insertProduct(product)
    }

    override suspend fun updateProduct(productTree: ProductTree) = withContext(dbCoroutineDispatcher) {
        db.productQueries.updateProduct(productTree)
    }
}
