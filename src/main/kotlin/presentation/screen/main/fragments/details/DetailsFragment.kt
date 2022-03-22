package presentation.screen.main.fragments.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.entity.ProductTree
import domain.model.MutableProductTreeNode
import presentation.components.Divider
import presentation.components.IconButton
import presentation.screen.main.components.ComponentDetail
import presentation.screen.main.components.MainFragmentTopBar
import presentation.screen.main.fragments.MainFragment

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.DetailsFragment(
    product: ProductTree?,
    onEditProduct: (ProductTree) -> Unit,
    onDeleteProduct: (ProductTree) -> Unit,
    onRunMrp: (ProductTree) -> Unit
) {
    MainFragment {
        Crossfade(product) { product ->
            if (product == null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("Nie wybrano produktu")
                }
            } else {
                Column {
                    MainFragmentTopBar(
                        title = product.node.name,
                        actions = {
                            CompositionLocalProvider(
                                LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            ) {
                                IconButton(
                                    icon = Icons.Default.Edit,
                                    onClick = { onEditProduct(product) }
                                )
                                IconButton(
                                    icon = Icons.Default.Delete,
                                    onClick = { onDeleteProduct(product) }
                                )
                            }
                            IconButton(
                                icon = Icons.Default.PlayArrow,
                                size = 30.dp,
                                tint = Color.Green.copy(alpha = 0.8f),
                                onClick = { onRunMrp(product) }
                            )
                        }
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 24.dp)
                    ) {
                        ComponentDetail(
                            detail = "czas produkcji",
                            value = product.node.leadTime
                        )
                        ComponentDetail(
                            detail = "na stanie",
                            value = product.node.inStock
                        )
                        ComponentDetail(
                            detail = "batch size",
                            value = product.node.batchSize
                        )
                    }
                    TextButton(
                        onClick = {},
                        modifier = Modifier.padding(start = 4.dp)
                    ) { Text("Dodaj komponent") }
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 500.dp
                        )
                    ) {
                        product.node.components.forEach(::componentsListItem)
                    }
                }
            }
        }
    }
}

fun LazyListScope.componentsListItem(component: MutableProductTreeNode) {
    val indentLevel = component.bom.toInt() - 1
    val startIndent = 40.dp * indentLevel
    item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = startIndent, end = 20.dp)
            ) {
                Surface(
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                ) { }
                Surface(
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .width(36.dp)
                        .height(1.dp)
                ) { }
            }
            Box {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(component.name)

                        IconButton(
                            icon = Icons.Default.Add,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            onClick = {}
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        ComponentDetail(
                            detail = "ilość",
                            value = component.requiredAmount
                        )
                        ComponentDetail(
                            detail = "czas produkcji",
                            value = component.leadTime
                        )
                        ComponentDetail(
                            detail = "na stanie",
                            value = component.inStock
                        )
                        ComponentDetail(
                            detail = "rozmiar partii",
                            value = component.batchSize
                        )
                    }
                }
            }
        }
    }
    item {
        Divider(startIndent = startIndent, tonalElevation = 2.dp)
    }
    for (child in component.components) {
        componentsListItem(child)
    }
}
