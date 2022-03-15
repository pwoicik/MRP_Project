package domain.model

typealias Bom = Int

data class ProductWithComponents(
    val product: Product,
    val components: Map<Bom, List<Component>>
)
