package presentation.screen.createComponent

import MrpApplication
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.components.EditComponentTextField

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateComponentScreen(
    stateFlow: StateFlow<CreateComponentState>,
    emit: (CreateComponentEvent) -> Unit
) {
    val state by stateFlow.collectAsState()

    val scope = rememberCoroutineScope()

    val onSubmit: () -> Unit = {
        scope.launch {
            MrpApplication().mrpRepository.insertProduct(
                name = state.name,
                leadTime = state.leadTime.toLong(),
                batchSize = state.batchSize.toLong(),
                inStock = state.inStock.toLong()
            )
            emit(CreateComponentEvent.GoBack)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create a product") },
                actions = {
                    IconButton(onClick = { emit(CreateComponentEvent.GoBack) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Save") },
                icon = { Icon(imageVector = Icons.Default.Save, contentDescription = "Save product") },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = onSubmit
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 36.dp)
                    .widthIn(max = 500.dp)
            ) {

                val focusRequester = remember { FocusRequester() }
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                EditComponentTextField(
                    value = state.name,
                    isError = state.name.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.NameChanged(it)) },
                    label = "name",
                    modifier = Modifier.focusRequester(focusRequester)
                )
                EditComponentTextField(
                    value = state.leadTime,
                    isError = state.leadTime.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.LeadTimeChanged(it)) },
                    label = "lead time",
                    isNumeric = true
                )
                EditComponentTextField(
                    value = state.batchSize,
                    isError = state.batchSize.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.BatchSizeChanged(it)) },
                    label = "batch size",
                    isNumeric = true
                )
                EditComponentTextField(
                    value = state.inStock,
                    isError = state.inStock.isBlank(),
                    onValueChange = { emit(CreateComponentEvent.InStockChanged(it)) },
                    label = "in stock",
                    isNumeric = true,
                    modifier = Modifier.onKeyEvent { ev ->
                        if (ev.key == Key.Enter) {
                            onSubmit()
                        }
                        true
                    }
                )
            }
        }
    }
}
