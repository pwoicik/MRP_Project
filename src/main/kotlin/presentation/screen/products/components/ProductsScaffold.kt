package presentation.screen.products.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScaffold(
    onFabClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Products") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add product") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add a new product"
                    )
                },
                onClick = onFabClick
            )
        },
        content = content
    )
}
