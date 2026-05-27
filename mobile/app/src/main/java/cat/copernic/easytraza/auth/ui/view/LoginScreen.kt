package cat.copernic.easytraza.auth.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.copernic.easytraza.R
import cat.copernic.easytraza.auth.model.UsuariDto
import cat.copernic.easytraza.auth.ui.viewmodel.LoginViewModel
import cat.copernic.easytraza.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val llistaUsuaris by viewModel.usuaris.collectAsState()
    val isError by viewModel.isError.collectAsState()
    val serverIp by viewModel.serverIpFlow.collectAsState(initial = "10.0.2.2")
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var usuariAEditarFoto by remember { mutableStateOf<String?>(null) }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null && usuariAEditarFoto != null) {
            viewModel.actualizarFotoPerfil(context, uri, usuariAEditarFoto!!)
            usuariAEditarFoto = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(clrSurface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(clrSurfaceContainerHighest)
                    .clickable { onNavigateToSettings() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings),
                    tint = clrPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.logo_easytraza),
                contentDescription = stringResource(R.string.logo_desc),
                modifier = Modifier.height(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.login_who),
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = clrOnSurface,
            letterSpacing = (-1).sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Text(
            text = stringResource(R.string.login_select_profile),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = clrOnSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )

        androidx.compose.material3.pulltorefresh.PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    viewModel.carregarUsuaris()
                    delay(1000)
                    isRefreshing = false
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            if (llistaUsuaris.isEmpty() && isError) {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = clrOnSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.login_error_profiles),
                                color = clrOnSurfaceVariant,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.login_pull_retry),
                                color = clrOnSurfaceVariant.copy(alpha = 0.5f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else if (llistaUsuaris.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = clrPrimary)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(llistaUsuaris) { usuari ->
                        PerfilCard(
                            usuari = usuari,
                            serverIp = serverIp,
                            onClick = {
                                viewModel.iniciarSessio(usuari) {
                                    onNavigateToHome()
                                }
                            },
                            onEditPhotoClick = {
                                usuariAEditarFoto = usuari.email
                                photoLauncher.launch("image/*")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PerfilCard(usuari: UsuariDto, serverIp: String, onClick: () -> Unit, onEditPhotoClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .background(Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(clrPrimary.copy(alpha = 0.1f))
        ) {
            if (usuari.fotoPerfilUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("http://$serverIp:8080${usuari.fotoPerfilUrl}")
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Text(
                    text = usuari.nom.take(1).uppercase(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = clrPrimary
                )
            }

            // Overlay icon to change photo
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(clrPrimary)
                    .clickable { onEditPhotoClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Cambiar Foto",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = usuari.nom,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = clrSecondary,
            letterSpacing = (-0.5).sp
        )
    }
}
