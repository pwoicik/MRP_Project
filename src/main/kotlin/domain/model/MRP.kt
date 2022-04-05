package domain.model

data class MRP(
    val name: String,
    val leadTime: Int,
    val onHand: Int,
    val batchSize: Int,
    val requiredAmount: Int,
    val bom: Int,
    val entries: List<MRPEntry>
)

data class MRPEntry(
    val grossRequirements: Int = 0,
    val scheduledReceipts: Int = 0,
    val predictedOnHand: Int = 0,
    val netRequirements: Int = 0,
    val plannedOrderReleases: Int = 0,
    val plannedOrderReceipts: Int = 0,
)
