package presentation.screen.product

import MrpApplication
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.launch
import presentation.components.NewItemTextField

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen() {
    val products by MrpApplication().mrpRepository.getAllTopLevelProducts().collectAsState(emptyList())

    var isDialogVisible by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add a product") },
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new product") },
                onClick = { isDialogVisible = true }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (isDialogVisible) {
                NewProductDialog(onCloseRequest = { isDialogVisible = false })
            }

            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    stickyHeader {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text("NAME", modifier = Modifier.weight(1f))
                            Text("LEAD TIME", modifier = Modifier.weight(1f))
                            Text("BATCH SIZE", modifier = Modifier.weight(1f))
                            Text("IN STOCK", modifier = Modifier.weight(1f))
                        }
                    }
                    items(products) { product ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(product.name, modifier = Modifier.weight(1f))
                            Text(product.leadTime.toString(), modifier = Modifier.weight(1f))
                            Text(product.batchSize.toString(), modifier = Modifier.weight(1f))
                            Text(product.inStock.toString(), modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProductDialog(
    onCloseRequest: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var leadTime by remember { mutableStateOf("") }
    var batchSize by remember { mutableStateOf("") }
    var inStock by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val state = rememberDialogState(size = DpSize(500.dp, 550.dp))
    Dialog(
        onCloseRequest = onCloseRequest,
        state = state
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Create a product") }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Save") },
                    icon = { Icon(imageVector = Icons.Default.Save, contentDescription = "Save product") },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = {
                        scope.launch {
                            MrpApplication().mrpRepository.insertProduct(
                                name = name,
                                leadTime = leadTime.toLong(),
                                batchSize = batchSize.toLong(),
                                inStock = inStock.toLong()
                            )
                            onCloseRequest()
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 36.dp)
            ) {

                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                NewItemTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "name",
                    modifier = Modifier.focusRequester(focusRequester)
                )
                NewItemTextField(
                    value = leadTime,
                    onValueChange = { leadTime = it },
                    label = "lead time"
                )
                NewItemTextField(
                    value = batchSize,
                    onValueChange = { batchSize = it },
                    label = "batch size"
                )
                NewItemTextField(
                    value = inStock,
                    onValueChange = { inStock = it },
                    label = "in stock"
                )
            }
        }
    }
}
