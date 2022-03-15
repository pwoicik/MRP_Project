package data.mapper

import data.entity.ProductEntity
import domain.model.Component
import domain.model.ProductWithComponents

fun List<ProductEntity>.toProductsWithComponents(): List<ProductWithComponents> {
    val productsByParent = groupBy(ProductEntity::parent)
    val topLevelProducts = productsByParent[null] ?: emptyList()

    val productsWithComponents = topLevelProducts
        .map { productEntity ->
            val componentEntities = productsByParent[productEntity.id] ?: emptyList()
            val componentsEntitiesByBom = componentEntities.groupBy(ProductEntity::bom)
            val componentsByBom = componentsEntitiesByBom
                .map { (bom, components) ->
                    bom.toInt() to components.map(ProductEntity::toComponent)
                }.toMap()

            ProductWithComponents(
                product = productEntity.toComponent(),
                components = componentsByBom
            )
        }

    return productsWithComponents
}

fun ProductEntity.toProduct() = toComponent()

fun ProductEntity.toComponent() = Component(
    id = id,
    name = name,
    leadTime = leadTime.toInt(),
    batchSize = batchSize.toInt(),
    inStock = inStock.toInt()
)
