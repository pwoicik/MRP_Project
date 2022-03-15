package data.repository

import data.entity.ProductEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import data.MrpDatabase
import data.mapper.toProduct
import data.mapper.toProductsWithComponents
import domain.model.Product
import domain.model.ProductWithComponents
import domain.repository.MrpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MrpRepositoryImpl(
    private val db: MrpDatabase
) : MrpRepository {

    override suspend fun deleteProduct(productId: Long) {
        db.productQueries.deleteProduct(productId)
    }

    override fun getAllProductsWithComponents(): Flow<List<ProductWithComponents>> {
        return db.productQueries.getAllProducts().asFlow().mapToList()
            .map(List<ProductEntity>::toProductsWithComponents)
    }

    override fun getAllTopLevelProducts(): Flow<List<Product>> {
        return db.productQueries.getAllTopLevelProducts().asFlow().mapToList()
            .map { it.map(ProductEntity::toProduct) }
    }

    override suspend fun getProduct(productId: Long): ProductEntity {
        return db.productQueries.getProduct(productId).executeAsOne()
    }

    override fun getProductWithComponents(productId: Long): Flow<ProductWithComponents?> {
        return db.productQueries.getProductWithComponents(productId).asFlow().mapToList()
            .map {
                val pwc = it.toProductsWithComponents()
                if (pwc.isEmpty()) null else pwc[0]
            }
    }

    override suspend fun insertProduct(product: ProductEntity) {
        db.productQueries.updateProduct(product)
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
}
