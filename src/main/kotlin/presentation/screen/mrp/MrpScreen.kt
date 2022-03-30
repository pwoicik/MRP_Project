package presentation.screen.mrp

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import presentation.components.IconButton
import presentation.components.TopBar
import presentation.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MrpScreen(
    viewModel: MrpViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                navigationIcon = {
                    IconButton(
                        icon = Icons.Default.ArrowBack,
                        onClick = navController::navigateUp,
                        size = 28.dp
                    )
                }
            ) {
                state.product?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = state.ghp == null,
            modifier = Modifier.padding(innerPadding)
        ) { isFetching ->
            when (isFetching) {
                true -> {}
                false -> {
                    val ghp = state.ghp!!
                    LazyColumn(
                        contentPadding = PaddingValues(32.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        stickyHeader {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = "GHP",
                                        style = MaterialTheme.typography.displaySmall
                                    )
                                    state.product?.let {
                                        Text("(na stanie: ${it.inStock};  czas produkcji: ${it.leadTime})")
                                    }
                                }

                                Surface(tonalElevation = 2.dp) {
                                    Row {
                                        TableHeader(text = "tydzień", textAlign = TextAlign.End)
                                        (1..ghp.entries.size).forEach {
                                            TableTextCell(
                                                text = it.toString(),
                                                textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            TableRow(headerText = "przewidywany popyt") {
                                ghp.entries.forEachIndexed { entryIndex, entry ->
                                    TableTextFieldCell(
                                        value = when (val av = entry.demand) {
                                            0 -> ""
                                            else -> av.toString()
                                        },
                                        onValueChange = { viewModel.demandChanged(index = entryIndex, newValue = it) }
                                    )
                                }
                            }
                        }
                        item {
                            TableRow(headerText = "produkcja") {
                                ghp.entries.forEachIndexed { entryIndex, entry ->
                                    TableTextFieldCell(
                                        value = when (val prod = entry.production) {
                                            0 -> ""
                                            else -> prod.toString()
                                        },
                                        onValueChange = {
                                            viewModel.productionChanged(
                                                index = entryIndex,
                                                newValue = it
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        item {
                            TableRow(headerText = "dostępne") {
                                ghp.entries.forEach {
                                    TableTextCell(it.availableAmount.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TableRow(
    headerText: String,
    content: @Composable RowScope.() -> Unit
) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        TableHeader(headerText)
        content()
    }
}

@Composable
fun RowScope.TableCell(
    weight: Float = 1f,
    content: @Composable () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            .fillMaxHeight()
            .weight(weight)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun RowScope.TableTextCell(
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Center,
    weight: Float = 1f
) {
    TableCell(weight = weight) {
        Text(
            text = text,
            style = textStyle,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RowScope.TableTextFieldCell(
    value: String,
    onValueChange: (String) -> Unit,
    weight: Float = 1f
) {
    val focusManager = LocalFocusManager.current
    TableCell(weight = weight) {
        TextField(
            value = TextFieldValue(value, selection = TextRange(value.length, value.length)),
            onValueChange = { onValueChange(it.text) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colorScheme.onSurface,
                focusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                cursorColor = MaterialTheme.colorScheme.secondary
            ),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            modifier = Modifier.onKeyEvent {
                if (it.type == KeyEventType.KeyUp) {
                    when (it.key) {
                        Key.Tab -> focusManager.moveFocus(
                            if (it.isShiftPressed) FocusDirection.Left else FocusDirection.Right
                        )
                        Key.Enter -> focusManager.moveFocus(
                            if (it.isShiftPressed) FocusDirection.Up else FocusDirection.Down
                        )
                        Key.DirectionLeft -> focusManager.moveFocus(FocusDirection.Left)
                        Key.DirectionRight -> focusManager.moveFocus(FocusDirection.Right)
                        Key.DirectionUp -> focusManager.moveFocus(FocusDirection.Up)
                        Key.DirectionDown -> focusManager.moveFocus(FocusDirection.Down)
                    }
                }
                true
            }
        )
    }
}

@Composable
fun RowScope.TableHeader(
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    TableTextCell(
        text = text,
        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
        textAlign = textAlign,
        weight = 2f
    )
}
