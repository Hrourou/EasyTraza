package cat.copernic.easytraza.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cat.copernic.easytraza.albaraproveidor.data.api.AlbaraproveidorService
import cat.copernic.easytraza.albaraproveidor.data.repository.AlbaraproveidorRepositoryImpl
import cat.copernic.easytraza.albaraproveidor.ui.view.AlbaraproveidorScreen
import cat.copernic.easytraza.albaraproveidor.ui.viewmodel.AlbaraproveidorViewModel
import cat.copernic.easytraza.auth.ui.view.CerrarSesionScreen
import cat.copernic.easytraza.auth.ui.view.LoginScreen
import cat.copernic.easytraza.auth.ui.viewmodel.LoginViewModel
import cat.copernic.easytraza.core.datastore.dataStore
import cat.copernic.easytraza.core.network.RetrofitClient
import cat.copernic.easytraza.dashboard.ui.view.DashboardScreen
import cat.copernic.easytraza.ip.data.repository.SettingsRepositoryImpl
import cat.copernic.easytraza.ip.ui.view.IpScreen
import cat.copernic.easytraza.ip.ui.viewmodel.IpViewModel
import cat.copernic.easytraza.lot.data.api.LotService
import cat.copernic.easytraza.lot.data.repository.LotRepositoryImpl
import cat.copernic.easytraza.lot.ui.view.FinalitzarLotScreen
import cat.copernic.easytraza.lot.ui.view.IniciarLotScreen
import cat.copernic.easytraza.lot.ui.view.DetallIniciarLotScreen
import cat.copernic.easytraza.lot.ui.view.DetallFinalitzarLotScreen
import cat.copernic.easytraza.lot.ui.viewmodel.LotViewModel
import cat.copernic.easytraza.ui.theme.EasyTrazaTheme
import cat.copernic.easytraza.ui.theme.clrSurface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyTrazaTheme {
                val context = LocalContext.current
                val settingsRepo = remember { SettingsRepositoryImpl(context.dataStore) }

                // Services
                val lotService = remember { RetrofitClient.createService(LotService::class.java, settingsRepo) }
                val albaraproveidorService = remember { RetrofitClient.createService(AlbaraproveidorService::class.java, settingsRepo) }

                // Repositories
                val lotRepo = remember { LotRepositoryImpl(lotService) }
                val albaraproveidorRepo = remember { AlbaraproveidorRepositoryImpl(albaraproveidorService) }

                // ViewModels
                val loginViewModel = remember { LoginViewModel(settingsRepo) }
                val ipViewModel = remember { IpViewModel(settingsRepo) }
                val lotViewModel = remember { LotViewModel(lotRepo, settingsRepo) }
                val albaraproveidorViewModel = remember { AlbaraproveidorViewModel(albaraproveidorRepo) }

                var currentScreen by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf("LOGIN") }
                var ipReturnScreen by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf("LOGIN") }

                val nomUsuari by settingsRepo.usuariNomFlow.collectAsState(initial = "Panadero")

                BackHandler(enabled = currentScreen != "DASHBOARD" && currentScreen != "LOGIN") {
                    when (currentScreen) {
                        "DETALL_INICIAR" -> currentScreen = "LLISTA_INICIAR"
                        "DETALL_FINALITZAR" -> currentScreen = "FINALITZAR"
                        "IP" -> currentScreen = ipReturnScreen
                        "LLISTA_INICIAR", "FINALITZAR", "RECEPCIONAR", "CERRAR_SESION" -> currentScreen = "DASHBOARD"
                        else -> currentScreen = "DASHBOARD"
                    }
                }

                Scaffold { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(clrSurface)
                    ) {
                        when (currentScreen) {
                            "LOGIN" -> LoginScreen(
                                viewModel = loginViewModel,
                                onNavigateToHome = { currentScreen = "DASHBOARD" },
                                onNavigateToSettings = {
                                    ipReturnScreen = "LOGIN"
                                    currentScreen = "IP"
                                }
                            )
                            "IP" -> IpScreen(
                                viewModel = ipViewModel,
                                onNavigateBack = { currentScreen = ipReturnScreen }
                            )
                            "DASHBOARD" -> DashboardScreen(
                                nomUsuari = nomUsuari ?: "Panadero",
                                onNavigate = { screen ->
                                    if (screen == "LLISTA_INICIAR" || screen == "FINALITZAR") {
                                        lotViewModel.carregarDades()
                                    }
                                    if (screen == "IP") {
                                        ipReturnScreen = "DASHBOARD"
                                    }
                                    currentScreen = screen
                                }
                            )
                            "LLISTA_INICIAR" -> IniciarLotScreen(
                                viewModel = lotViewModel,
                                onNavigateToDetail = { currentScreen = "DETALL_INICIAR" },
                                onNavigateBack = { currentScreen = "DASHBOARD" }
                            )
                            "DETALL_INICIAR" -> DetallIniciarLotScreen(
                                viewModel = lotViewModel,
                                onNavigateBack = { currentScreen = "LLISTA_INICIAR" },
                                onNavigateHome = { currentScreen = "DASHBOARD" },
                                onLotIniciat = { currentScreen = "LLISTA_INICIAR" }
                            )
                            "FINALITZAR" -> FinalitzarLotScreen(
                                viewModel = lotViewModel,
                                onNavigateToDetail = { currentScreen = "DETALL_FINALITZAR" },
                                onNavigateBack = { currentScreen = "DASHBOARD" }
                            )
                            "DETALL_FINALITZAR" -> DetallFinalitzarLotScreen(
                                viewModel = lotViewModel,
                                onNavigateBack = { currentScreen = "FINALITZAR" },
                                onNavigateHome = { currentScreen = "DASHBOARD" },
                                onLotFinalitzat = { currentScreen = "DASHBOARD" }
                            )
                            "CERRAR_SESION" -> CerrarSesionScreen(
                                onNavigateBack = { currentScreen = "DASHBOARD" },
                                onLogout = { currentScreen = "LOGIN" }
                            )
                            "RECEPCIONAR" -> {
                                val serverIp by settingsRepo.serverIpFlow.collectAsState(initial = "10.0.2.2")
                                AlbaraproveidorScreen(
                                    viewModel = albaraproveidorViewModel,
                                    serverIp = serverIp,
                                    onNavigateBack = { currentScreen = "DASHBOARD" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
