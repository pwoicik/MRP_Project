package presentation.screen.main.fragments.details

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import data.entity.ProductEntity
import domain.model.Component
import presentation.components.Divider
import presentation.components.IconButton
import presentation.screen.main.components.ComponentDetail
import presentation.screen.main.components.MainFragmentTopBar
import presentation.screen.main.fragments.MainFragment

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.DetailsFragment(
    product: ProductEntity?,
    onEditProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit,
    onAddComponent: (ProductEntity) -> Unit,
    onAddSubcomponent: (Component) -> Unit,
    onEditComponent: (Component) -> Unit,
    onDeleteComponent: (Component) -> Unit,
    onRunMrp: (ProductEntity) -> Unit
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
                        title = product.name,
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
                            value = product.leadTime
                        )
                        ComponentDetail(
                            detail = "na stanie",
                            value = product.inStock
                        )
                    }

                    TextButton(
                        onClick = { onAddComponent(product) },
                        modifier = Modifier.padding(start = 4.dp)
                    ) { Text("Dodaj komponent") }

                    val components = product.components
                    if (components.isEmpty()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("Brak komponentów")
                        }
                    } else {
                        Row {
                            val state = rememberLazyListState()
                            LazyColumn(
                                state = state,
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(components) { component ->
                                    ComponentListItem(
                                        component = component,
                                        onEditComponent = { onEditComponent(component) },
                                        onDeleteComponent = { onDeleteComponent(component) },
                                        onAddComponent = { onAddSubcomponent(component) }
                                    )
                                }
                            }
                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(state),
                                style = defaultScrollbarStyle().copy(
                                    unhoverColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    hoverColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComponentListItem(
    component: Component,
    onEditComponent: () -> Unit,
    onDeleteComponent: () -> Unit,
    onAddComponent: () -> Unit
) {
    Column {
        val indentLevel = component.bom.toInt() - 1
        val startIndent = 40.dp * indentLevel
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
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = component.name,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            icon = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            size = 20.dp,
                            onClick = onEditComponent
                        )
                        IconButton(
                            icon = Icons.Default.Delete,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            size = 20.dp,
                            onClick = onDeleteComponent
                        )
                        IconButton(
                            icon = Icons.Default.Add,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            onClick = onAddComponent
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
        Divider(startIndent = startIndent, tonalElevation = 2.dp)
    }
}
