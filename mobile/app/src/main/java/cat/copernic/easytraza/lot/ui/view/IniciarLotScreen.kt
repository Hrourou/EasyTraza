package cat.copernic.easytraza.lot.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.copernic.easytraza.R
import cat.copernic.easytraza.lot.ui.viewmodel.LotViewModel
import cat.copernic.easytraza.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarLotScreen(
    viewModel: LotViewModel,
    onNavigateToDetail: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val lotsEnEstoc by viewModel.lotsEnEstoc.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.carregarDades()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(clrSurface)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(clrSurfaceContainerHighest)
                    .clickable { onNavigateBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.btn_back),
                    tint = clrPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(clrSurfaceContainerHighest)
                    .clickable { onNavigateBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = stringResource(R.string.btn_home),
                    tint = clrPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.lot_new_process),
            fontSize = 12.sp,
            color = clrSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.lot_start_title),
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            color = clrOnSurface,
            letterSpacing = (-1).sp,
            lineHeight = 38.sp
        )
        Text(
            text = stringResource(R.string.lot_start_desc),
            color = clrOnSurfaceVariant,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        androidx.compose.material3.pulltorefresh.PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    viewModel.recarregarDades()
                    delay(1000)
                    isRefreshing = false
                }
            },
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            if (lotsEnEstoc.isEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                tint = clrOnSurfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.lot_empty_stock),
                                color = clrOnSurfaceVariant,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lotsEnEstoc) { lot ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.seleccionarLot(lot)
                                    onNavigateToDetail()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.lot_prefix, lot.id),
                                        fontWeight = FontWeight.Black,
                                        fontSize = 22.sp,
                                        color = clrPrimary,
                                        letterSpacing = (-0.5).sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = lot.materiaPrimera?.nom ?: stringResource(R.string.lot_mat_prima),
                                        color = clrOnSurfaceVariant,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = lot.proveidorNom ?: lot.proveidorCif ?: "",
                                        color = clrOnSurfaceVariant.copy(alpha = 0.8f),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DetallIniciarLotScreen(
    viewModel: LotViewModel,
    onNavigateBack: () -> Unit,
    onNavigateHome: () -> Unit,
    onLotIniciat: () -> Unit
) {
    val lot = viewModel.lotSeleccionat
    val lotPendent = viewModel.lotPendentDIniciar
    val dataAvui = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    if (lot == null) { onNavigateBack(); return }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.missatgeError) {
        viewModel.missatgeError?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(msg)
                viewModel.netejarAvisos()
            }
        }
    }

    if (lotPendent != null) {
        AlertDialog(
            onDismissRequest = { viewModel.netejarAvisos() },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = clrSecondary) },
            title = { Text(stringResource(R.string.lot_alert_open_title)) },
            text = { Text(stringResource(R.string.lot_alert_open_desc)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.iniciarLot(lotPendent, true)
                        onLotIniciat()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = clrPrimary)
                ) { Text(stringResource(R.string.lot_alert_open_confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.netejarAvisos() }) { Text(stringResource(R.string.btn_cancel)) }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(clrSurface)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(clrSurfaceContainerHighest)
                    .clickable { onNavigateBack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.btn_back),
                    tint = clrPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(clrSurfaceContainerHighest)
                    .clickable { onNavigateHome() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = stringResource(R.string.btn_home),
                    tint = clrPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.lot_config),
            fontSize = 12.sp,
            color = clrSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.lot_start_config_title),
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            color = clrOnSurface,
            letterSpacing = (-1).sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = clrSurfaceContainerLow),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(R.string.lot_prod_date),
                    fontSize = 11.sp,
                    color = clrOnSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Text(
                    dataAvui,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = clrOnSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.lot_to_start),
                    fontSize = 11.sp,
                    color = clrOnSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Text(
                    "#${lot.id}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = clrPrimary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "PROVEÏDOR", // Using hardcoded string temporarily, or we could add string resource
                    fontSize = 11.sp,
                    color = clrOnSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Text(
                    text = lot.proveidorNom ?: "Desconegut",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = clrOnSurface
                )
                if (lot.proveidorCif != null) {
                    Text(
                        text = "CIF: ${lot.proveidorCif}",
                        fontSize = 14.sp,
                        color = clrOnSurfaceVariant,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.lot_mat_associated),
                    fontSize = 11.sp,
                    color = clrOnSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
                Text(
                    text = lot.materiaPrimera?.nom ?: stringResource(R.string.lot_unknown),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = clrOnSurface
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.iniciarLot(lot, false, onSuccess = { onLotIniciat() }) },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = clrPrimary)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(stringResource(R.string.lot_btn_start), fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onNavigateBack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = clrPrimary)
        ) {
            Text(stringResource(R.string.btn_cancel), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
