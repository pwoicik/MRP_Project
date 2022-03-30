package domain.model

data class GHP(
    val name: String,
    val leadTime: Int,
    val onHand: Int,
    val entries: List<GHPEntry>
)

data class GHPEntry(
    val demand: Int,
    val production: Int,
    val availableAmount: Int
)
