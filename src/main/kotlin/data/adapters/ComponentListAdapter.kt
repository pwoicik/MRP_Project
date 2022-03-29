package data.adapters

import com.squareup.sqldelight.ColumnAdapter
import domain.model.Component
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ComponentListAdapter : ColumnAdapter<List<Component>, String> {
    override fun decode(databaseValue: String): List<Component> {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: List<Component>): String {
        return Json.encodeToString(value)
    }
}
