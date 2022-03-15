package presentation.screen.products

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import domain.model.Product
import kotlinx.coroutines.flow.StateFlow
import presentation.screen.products.components.ProductsScaffold
import presentation.screen.products.components.ProductsTable

@Composable
fun ProductsScreen(
    products: StateFlow<List<Product>>,
    onCreateProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onGoToProduct: (Product) -> Unit
) {
    @Suppress("NAME_SHADOWING")
    val products by products.collectAsState()


    ProductsScaffold(onFabClick = onCreateProduct) { innerPadding ->
        ProductsTable(
            products = products,
            onEditProductClick = onEditProduct,
            onAddComponentClick = {},
            onGoToProductClick = onGoToProduct,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
