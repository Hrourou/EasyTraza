package cat.copernic.easytraza.albaraproveidor.ui.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.copernic.easytraza.R
import cat.copernic.easytraza.albaraproveidor.ui.viewmodel.AlbaraproveidorUiState
import cat.copernic.easytraza.albaraproveidor.ui.viewmodel.AlbaraproveidorViewModel
import cat.copernic.easytraza.ui.theme.*
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbaraproveidorScreen(
    viewModel: AlbaraproveidorViewModel,
    serverIp: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val form = viewModel.formulari
    val uiState = viewModel.uiState

    var expProveidor by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) viewModel.processarImatgeOcr(context, uri)
    }

    when (uiState) {
        is AlbaraproveidorUiState.Error -> {
            AlertDialog(
                onDismissRequest = { viewModel.netejarAvisos() },
                title = { Text(stringResource(R.string.alert_error)) },
                text = { Text(uiState.message) },
                confirmButton = { TextButton(onClick = { viewModel.netejarAvisos() }) { Text(stringResource(R.string.btn_ok)) } }
            )
        }
        is AlbaraproveidorUiState.Success -> {
            AlertDialog(
                onDismissRequest = { viewModel.netejarAvisos() },
                title = { Text(stringResource(R.string.alert_success), color = Color(0xFF388E3C)) },
                text = { Text(uiState.message) },
                confirmButton = {
                    Button(
                        onClick = { viewModel.netejarAvisos() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                    ) {
                        Text(stringResource(R.string.btn_ok), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(clrSurface)
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
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
                    .clip(androidx.compose.foundation.shape.CircleShape)
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
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.rec_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = clrOnSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(stringResource(R.string.rec_desc), color = clrOnSurfaceVariant, modifier = Modifier.padding(bottom = 24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(clrSurfaceContainerLow)
                .border(2.dp, clrPrimary.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .clickable(enabled = uiState !is AlbaraproveidorUiState.Loading) { galleryLauncher.launch("image/*") }
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (uiState is AlbaraproveidorUiState.Loading) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = clrPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.rec_processing), fontSize = 13.sp, color = clrOnSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))

                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (form.fotoAlbaraUrl != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("http://$serverIp:8080${form.fotoAlbaraUrl}")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Albarán procesado",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Cambiar imagen seleccionada", fontSize = 12.sp, color = clrPrimary)
                    } else {
                        Icon(Icons.Default.Add, contentDescription = null, tint = clrPrimary, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.rec_select), fontWeight = FontWeight.Bold, color = clrOnSurface)
                        Text(stringResource(R.string.rec_select_desc), fontSize = 12.sp, color = clrOnSurfaceVariant)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(24.dp)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(stringResource(R.string.rec_doc_data), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = clrOnSurface, modifier = Modifier.padding(bottom = 16.dp))

                ExposedDropdownMenuBox(
                    expanded = expProveidor,
                    onExpandedChange = { expProveidor = !expProveidor }
                ) {
                    OutlinedTextField(
                        value = form.cifProveidor,
                        onValueChange = {
                            viewModel.updateCif(it)
                            expProveidor = true
                        },
                        label = { Text(stringResource(R.string.rec_prov_cif)) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expProveidor) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    if (viewModel.llistaProveidors.isNotEmpty() && expProveidor) {
                        ExposedDropdownMenu(
                            expanded = expProveidor,
                            onDismissRequest = { expProveidor = false }
                        ) {
                            val proveedoresFiltrados = viewModel.llistaProveidors.filter {
                                it.cif.contains(form.cifProveidor, ignoreCase = true) ||
                                        (it.nom?.contains(form.cifProveidor, ignoreCase = true) ?: false)
                            }

                            proveedoresFiltrados.forEach { prov ->
                                DropdownMenuItem(
                                    text = { Text("${prov.cif} - ${prov.nom ?: "Sense nom"}") },
                                    onClick = {
                                        viewModel.updateCif(prov.cif)
                                        expProveidor = false
                                    }
                                )
                            }
                            if (proveedoresFiltrados.isEmpty()) {
                                DropdownMenuItem(text = { Text(stringResource(R.string.rec_prov_not_found), color = Color.Gray) }, onClick = { expProveidor = false })
                            }
                        }
                    }
                }

                if (viewModel.isProveidorExistent) {
                    Text(
                        text = stringResource(R.string.rec_prov_registered, form.nomProveidor ?: ""),
                        color = Color(0xFF2E7D32),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp, bottom = 8.dp)
                    )
                }
                else if (form.cifProveidor.isNotBlank()) {
                    Text(
                        text = stringResource(R.string.rec_prov_new),
                        color = Color(0xFFD84315),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp, bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = form.nomProveidor ?: "",
                        onValueChange = { viewModel.updateNomProveidor(it) },
                        label = { Text(stringResource(R.string.rec_prov_name_new)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = form.dataRecepcio,
                    onValueChange = { viewModel.updateData(it) },
                    label = { Text(stringResource(R.string.rec_date)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(R.string.rec_prod_lines), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = clrOnSurface)
        Spacer(modifier = Modifier.height(8.dp))

        form.liniesLote.forEachIndexed { index, linia ->
            var expMateria by remember(index) { mutableStateOf(false) }

            Card(
                colors = CardDefaults.cardColors(containerColor = clrSurfaceContainerLow),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(R.string.rec_row, index + 1), fontWeight = FontWeight.Black, fontSize = 12.sp, color = clrPrimary)
                        if (form.liniesLote.size > 1) {
                            IconButton(onClick = { viewModel.removeLiniaLote(index) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.rec_btn_delete), tint = Color.Red)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expMateria,
                        onExpandedChange = { expMateria = !expMateria }
                    ) {
                        OutlinedTextField(
                            value = linia.nomMateriaPrimeraDetectada ?: "",
                            onValueChange = {
                                viewModel.updateMateria(index, it)
                                expMateria = true
                            },
                            label = { Text(stringResource(R.string.lot_mat_prima)) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expMateria) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )

                        if (viewModel.llistaMateries.isNotEmpty()) {
                            ExposedDropdownMenu(
                                expanded = expMateria,
                                onDismissRequest = { expMateria = false }
                            ) {
                                val materiesFiltrades = viewModel.llistaMateries.filter {
                                    it.nom.contains(linia.nomMateriaPrimeraDetectada ?: "", ignoreCase = true)
                                }

                                materiesFiltrades.forEach { mat ->
                                    DropdownMenuItem(
                                        text = { Text(mat.nom) },
                                        onClick = {
                                            viewModel.updateMateria(index, mat.nom)
                                            expMateria = false
                                        }
                                    )
                                }
                                if (materiesFiltrades.isEmpty()) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.rec_mat_not_found), color = Color.Gray) },
                                        onClick = { expMateria = false }
                                    )
                                }
                            }
                        }
                    }

                    val isNovaMateria = viewModel.llistaMateries.none { it.nom.equals(linia.nomMateriaPrimeraDetectada, ignoreCase = true) } && !linia.nomMateriaPrimeraDetectada.isNullOrEmpty()
                    if (isNovaMateria) {
                        Text(
                            text = stringResource(R.string.rec_mat_new),
                            color = Color(0xFF0277BD),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = linia.idLot,
                            onValueChange = { viewModel.updateLot(index, it) },
                            label = { Text(stringResource(R.string.rec_lot_number)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        OutlinedTextField(
                            value = linia.unitats?.toString() ?: "",
                            onValueChange = { newValue ->
                                val numbersOnly = newValue.filter { it.isDigit() }
                                viewModel.updateUnitats(index, numbersOnly)
                            },
                            label = { Text(stringResource(R.string.rec_units)) },
                            modifier = Modifier.weight(0.5f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }
            }
        }

        TextButton(onClick = { viewModel.addLiniaLote() }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.rec_btn_add_row), color = clrPrimary, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.guardarAlbara() },
            modifier = Modifier.fillMaxWidth().height(70.dp),
            shape = RoundedCornerShape(35.dp),
            colors = ButtonDefaults.buttonColors(containerColor = clrPrimary),
            enabled = uiState !is AlbaraproveidorUiState.Loading
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(stringResource(R.string.rec_btn_save), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}
