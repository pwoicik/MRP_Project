package domain.model

data class MRP(
    val name: String,
    val grossRequirements: Int,
    val scheduledReceipts: Int,
    val predictedOnHand: Int,
    val netRequirements: Int,
    val plannedOrderReleases: Int,
    val plannedOrderReceipts: Int,
    val leadTime: Int,
    val onHand: Int,
    val batchSize: Int,
    val requiredAmount: Int,
    val bom: Int
)
