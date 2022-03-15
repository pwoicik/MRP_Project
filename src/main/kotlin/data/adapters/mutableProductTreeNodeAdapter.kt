package data.adapters

import com.squareup.sqldelight.ColumnAdapter
import domain.model.MutableProductTreeNode
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

val mutableProductTreeNodeAdapter = object : ColumnAdapter<MutableProductTreeNode, String> {
    override fun decode(databaseValue: String): MutableProductTreeNode {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: MutableProductTreeNode): String {
        return Json.encodeToString(value)
    }
}
