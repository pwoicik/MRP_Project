package presentation.screen.mrp

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import domain.model.GHP
import domain.model.MRP
import presentation.components.IconButton
import presentation.components.TopBar
import presentation.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.displayMedium
                        )
                        Column {
                            ProvideTextStyle(LocalTextStyle.current.copy(fontStyle = FontStyle.Italic)) {
                                Text("na stanie: ${it.inStock}")
                                Text("czas produkcji: ${it.leadTime}")
                            }
                        }
                    }
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
                    Column(modifier = Modifier) {
                        val horizontalScrollState = rememberScrollState()
                        Row(modifier = Modifier.weight(1f)) {
                            val lazyListState = rememberLazyListState()
                            LazyColumn(
                                state = lazyListState,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .horizontalScroll(horizontalScrollState)
                            ) {
                                ghp(
                                    ghp = state.ghp!!,
                                    onDemandChange = viewModel::demandChanged,
                                    onProductionChange = viewModel::productionChanged
                                )

                                state.mrps!!.forEachIndexed { idx, mrp ->
                                    item { Spacer(Modifier.height(128.dp)) }
                                    mrp(
                                        mrp = mrp,
                                        onScheduledReceiptsChange = { entryIdx, newValue ->
                                            viewModel.scheduledReceiptsChanged(idx, entryIdx, newValue)
                                        }
                                    )
                                }
                            }
                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(lazyListState),
                                style = LocalScrollbarStyle.current.copy(
                                    unhoverColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    hoverColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            )
                        }
                        HorizontalScrollbar(
                            adapter = rememberScrollbarAdapter(horizontalScrollState),
                            style = LocalScrollbarStyle.current.copy(
                                unhoverColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                hoverColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.ghp(
    ghp: GHP,
    onDemandChange: (index: Int, newValue: String) -> Unit,
    onProductionChange: (index: Int, newValue: String) -> Unit
) {
    stickyHeader {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
        ) {
            Text(
                text = "GHP",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 48.dp)
            )
        }
    }

    item {
        TableLayout(
            nColumns = ghp.entries.size + 1,
            modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            TableHeader(text = "tydzień", textAlign = TextAlign.End)
            (1..ghp.entries.size).forEach {
                TableHeader(it.toString(), textAlign = TextAlign.Center)
            }

            TableHeader("przewidywany popyt")
            ghp.entries.forEachIndexed { idx, entry ->
                TableTextFieldCell(
                    value = when (val av = entry.demand) {
                        0 -> ""
                        else -> av.toString()
                    },
                    onValueChange = { onDemandChange(idx, it) }
                )
            }

            TableHeader("produkcja")
            ghp.entries.forEachIndexed { idx, entry ->
                TableTextFieldCell(
                    value = when (val prod = entry.production) {
                        0 -> ""
                        else -> prod.toString()
                    },
                    onValueChange = { onProductionChange(idx, it) }
                )
            }

            TableHeader("dostępne")
            ghp.entries.forEach {
                TableTextCell(it.availableAmount.toString())
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.mrp(
    mrp: MRP,
    onScheduledReceiptsChange: (entryIdx: Int, newValue: String) -> Unit
) {
    stickyHeader {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
        ) {
            Text(
                text = "${mrp.name}  (${mrp.requiredAmount})",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 48.dp)
            )
        }
    }

    item {
        TableLayout(
            nColumns = mrp.entries.size + 1,
            modifier = Modifier.border(2.dp, MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Surface(tonalElevation = 2.dp) {
                TableCell {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProvideTextStyle(LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)) {
                            Text(
                                text = "okres",
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "dane produkcyjne",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .width(IntrinsicSize.Max)
                                    .padding(end = 24.dp)
                            )
                        }
                    }
                }
            }
            (1..mrp.entries.size).forEach {
                TableHeader(it.toString(), textAlign = TextAlign.Center)
            }

            TableHeader("całkowite zapotrzebowanie")
            mrp.entries.forEach {
                TableTextCell(it.grossRequirements.if0thenEmpty())
            }

            TableHeader("planowane przyjęcia")
            mrp.entries.forEachIndexed { idx, entry ->
                TableTextFieldCell(
                    value = when (val av = entry.scheduledReceipts) {
                        0 -> ""
                        else -> av.toString()
                    },
                    onValueChange = { onScheduledReceiptsChange(idx, it) }
                )
            }

            TableHeader("przewidywane na stanie")
            mrp.entries.forEach {
                TableTextCell(it.predictedOnHand.toString())
            }

            TableHeader("zapotrzebowanie netto")
            mrp.entries.forEach {
                TableTextCell(it.netRequirements.if0thenEmpty())
            }

            TableHeader("planowane zamówienia")
            mrp.entries.forEach {
                TableTextCell(it.plannedOrderReleases.if0thenEmpty())
            }

            TableHeader("planowane przyjęcie zamówień")
            mrp.entries.forEach {
                TableTextCell(it.plannedOrderReceipts.if0thenEmpty())
            }
        }
        Surface {
            Row {
                TableTextCell("Czas realizacji = ${mrp.leadTime}")
                TableTextCell("Wielkość partii = ${mrp.batchSize}")
                TableTextCell("Poziom BOM = ${mrp.bom}")
                TableTextCell("Na stanie = ${mrp.onHand}")
            }
        }
    }
}

@Composable
fun TableHeader(
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Surface(tonalElevation = 2.dp) {
        TableTextCell(
            text = text,
            textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
            textAlign = textAlign
        )
    }
}

@Composable
fun TableTextCell(
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    textAlign: TextAlign = TextAlign.Center
) {
    TableCell {
        Text(
            text = text,
            style = textStyle,
            textAlign = textAlign,
            modifier = Modifier
                .fillMaxWidth()
                .width(IntrinsicSize.Max)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TableTextFieldCell(
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TableCell {
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
            modifier = Modifier
                .widthIn(max = 128.dp)
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyUp) {
                        when (it.key) {
                            Key.Tab -> focusManager.moveFocus(
                                if (it.isShiftPressed) FocusDirection.Left else FocusDirection.Right
                            )
                            Key.Enter -> focusManager.moveFocus(
                                if (it.isShiftPressed) FocusDirection.Up else FocusDirection.Down
                            )
                            else -> {
                                if (!it.isCtrlPressed) return@onKeyEvent false
                                when (it.key) {
                                    Key.DirectionLeft -> focusManager.moveFocus(FocusDirection.Left)
                                    Key.DirectionRight -> focusManager.moveFocus(FocusDirection.Right)
                                    Key.DirectionUp -> focusManager.moveFocus(FocusDirection.Up)
                                    Key.DirectionDown -> focusManager.moveFocus(FocusDirection.Down)
                                }
                            }
                        }
                    }
                    false
                }
        )
    }
}

@Composable
fun TableCell(content: @Composable () -> Unit) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun TableLayout(
    nColumns: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val nRows = measurables.size / nColumns

        val measurableColumns = List(nColumns) { x ->
            List(nRows) { y ->
                measurables[y * nColumns + x]
            }
        }

        var columnWidths = measurableColumns.map { column ->
            column.maxOf { it.minIntrinsicWidth(Constraints.Infinity) }
        }

        val rowHeight = measurableColumns.mapIndexed { x, col ->
            col.maxOf { it.minIntrinsicHeight(columnWidths[x]) }
        }.maxOf { it }

        columnWidths = columnWidths.map { it.coerceAtLeast(rowHeight) }

        val placeableColumns = measurableColumns.mapIndexed { x, col ->
            col.map { measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = columnWidths[x],
                        maxWidth = columnWidths[x],
                        minHeight = rowHeight,
                        maxHeight = rowHeight
                    )
                )
            }
        }

        layout(columnWidths.sum(), rowHeight * nRows) {
            var x = 0

            placeableColumns.forEachIndexed { i, column ->
                var y = 0
                for (placeable in column) {
                    placeable.placeRelative(x, y)
                    y += rowHeight
                }
                x += columnWidths[i]
            }
        }
    }
}

private fun Int.if0thenEmpty() = if (this == 0) "" else this.toString()
