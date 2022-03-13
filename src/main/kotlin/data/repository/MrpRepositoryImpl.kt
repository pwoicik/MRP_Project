package data.repository

import MrpDatabase.Product
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import data.MrpDatabase
import domain.repository.MrpRepository
import kotlinx.coroutines.flow.Flow

class MrpRepositoryImpl(
    private val db: MrpDatabase
) : MrpRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return db.productQueries.getAllProducts().asFlow().mapToList()
    }

    override fun getAllTopLevelProducts(): Flow<List<Product>> {
        return db.productQueries.getAllTopLevelProducts().asFlow().mapToList()
    }

    override suspend fun insertProduct(
        name: String,
        leadTime: Long,
        batchSize: Long,
        inStock: Long,
        bom: Long,
        parent: Long?,
        id: Long?
    ) {
        db.productQueries.insertProduct(name, leadTime, batchSize, inStock, bom, parent)
    }

    override suspend fun insertProduct(product: Product) {
        db.productQueries.updateProduct(product)
    }
}
