package com.example.envasistema.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.envasistema.ui.screens.LoginScreen
import com.example.envasistema.ui.screens.home.HomeScreen
import com.example.envasistema.ui.screens.ingresos.*
import com.example.envasistema.ui.screens.movimientos.TransferenciaInventarioScreen
import com.example.envasistema.ui.screens.salidas.*
import com.example.envasistema.ui.screens.transformaciones.DesarmarPaquetesScreen
import com.amplifyframework.core.Amplify
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    
    // Ingresos
    object Ingresos : Screen("ingresos")
    object ProduccionNueva : Screen("produccion_nueva")
    object IngresoArmado : Screen("ingreso_armado")
    object DevolucionNoArmado : Screen("devolucion_no_armado")
    object DevolucionMercaderia : Screen("devolucion_mercaderia")
    
    // Salidas
    object Salidas : Screen("salidas")
    object VentaPT : Screen("venta_pt")
    object ArmarPaquetes : Screen("armar_paquetes")
    object MermaMolino : Screen("merma_molino")
    object Donaciones : Screen("donaciones")
    
    // Others
    object Movimientos : Screen("movimientos")
    object Transformaciones : Screen("transformaciones")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }
    
    // Check if user is logged in
    LaunchedEffect(Unit) {
        Amplify.Auth.fetchAuthSession(
            { session ->
                startDestination = if (session.isSignedIn) Screen.Home.route else Screen.Login.route
            },
            { error ->
                Log.e("Auth", "Session check failed", error)
                startDestination = Screen.Login.route
            }
        )
    }

    if (startDestination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(navController = navController, startDestination = startDestination!!) {
            composable(Screen.Login.route) {
                LoginScreen(onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    onIngresosClick = { navController.navigate(Screen.Ingresos.route) },
                    onSalidasClick = { navController.navigate(Screen.Salidas.route) },
                    onMovimientosClick = { navController.navigate(Screen.Movimientos.route) },
                    onTransformacionesClick = { navController.navigate(Screen.Transformaciones.route) },
                    onLogoutClick = {
                        Amplify.Auth.signOut {
                            Log.i("Auth", "Signed out")
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // --- INGRESOS ---
            composable(Screen.Ingresos.route) {
                IngresosScreen(
                    onBackClick = { navController.popBackStack() },
                    onProduccionNuevaClick = { navController.navigate(Screen.ProduccionNueva.route) },
                    onIngresoArmadoClick = { navController.navigate(Screen.IngresoArmado.route) },
                    onDevolucionNoArmadoClick = { navController.navigate(Screen.DevolucionNoArmado.route) },
                    onDevolucionMercaderiaClick = { navController.navigate(Screen.DevolucionMercaderia.route) }
                )
            }
            composable(Screen.ProduccionNueva.route) {
                ProduccionNuevaScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.IngresoArmado.route) {
                IngresoArmadoScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.DevolucionNoArmado.route) {
                DevolucionNoArmadoScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.DevolucionMercaderia.route) {
                DevolucionMercaderiaScreen(onBackClick = { navController.popBackStack() })
            }

            // --- SALIDAS ---
            composable(Screen.Salidas.route) {
                SalidasScreen(
                    onBackClick = { navController.popBackStack() },
                    onVentaPTClick = { navController.navigate(Screen.VentaPT.route) },
                    onArmarPaquetesClick = { navController.navigate(Screen.ArmarPaquetes.route) },
                    onMermaMolinoClick = { navController.navigate(Screen.MermaMolino.route) },
                    onDonacionesClick = { navController.navigate(Screen.Donaciones.route) }
                )
            }
            composable(Screen.VentaPT.route) {
                VentaPTScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.ArmarPaquetes.route) {
                ArmarPaquetesScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.MermaMolino.route) {
                MermaMolinoScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Donaciones.route) {
                DonacionesScreen(onBackClick = { navController.popBackStack() })
            }

            // --- MOVIMIENTOS ---
            composable(Screen.Movimientos.route) {
                TransferenciaInventarioScreen(onBackClick = { navController.popBackStack() })
            }

            // --- TRANSFORMACIONES ---
            composable(Screen.Transformaciones.route) {
                DesarmarPaquetesScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
