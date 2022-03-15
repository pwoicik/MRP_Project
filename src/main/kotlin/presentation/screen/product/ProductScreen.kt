package presentation.screen.product

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import domain.model.ProductWithComponents
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    productWithComponentsStateFlow: StateFlow<ProductWithComponents?>,
    onEditProduct: () -> Unit,
    onDeleteProduct: () -> Unit,
    onGoBack: () -> Unit
) {
    val productWithComponents by productWithComponentsStateFlow.collectAsState()

    @Suppress("NAME_SHADOWING")
    productWithComponents?.let { productWithComponents ->
        val (product, components) = productWithComponents
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text(product.name) },
                    actions = {
                        IconButton(onClick = onEditProduct) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit product"
                            )
                        }
                        IconButton(onClick = onDeleteProduct) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete product"
                            )
                        }
                        IconButton(onClick = onGoBack) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Go back"
                            )
                        }
                    }
                )
            }
        ) {

        }
    }
}
