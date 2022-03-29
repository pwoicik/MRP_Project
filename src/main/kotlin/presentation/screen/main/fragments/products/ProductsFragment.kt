package presentation.screen.main.fragments.products

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIconDefaults
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import data.entity.ProductEntity
import presentation.screen.main.components.MainFragmentTopBar
import presentation.screen.main.fragments.MainFragment
import presentation.screen.main.components.ProductCard

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.ProductsFragment(
    products: List<ProductEntity>?,
    onAddProductClick: () -> Unit,
    onProductClick: (ProductEntity) -> Unit
) {
    MainFragment {
        MainFragmentTopBar(
            title = "Produkty",
            actions = {
                TextButton(
                    onClick = onAddProductClick,
                    modifier = Modifier.pointerHoverIcon(PointerIconDefaults.Hand)
                ) {
                    Text("Dodaj produkt")
                }
            }
        )
        products?.let { products ->
            Crossfade(products.isEmpty()) {
                if (it) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Brak zapisanych produktów")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 48.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onClick = { onProductClick(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}
