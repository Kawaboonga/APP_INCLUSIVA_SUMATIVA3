package com.example.ref01.ui.screens.devicefinder

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ref01.domain.location.LocationUiState
import com.example.ref01.ui.components.ScreenScaffold
import com.example.ref01.ui.viewmodels.LocationViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.util.Locale

/** ================= Helpers & permisos ================= */

private val LOCATION_PERMS = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION
)

private fun hasLocationPermission(ctx: android.content.Context): Boolean =
    LOCATION_PERMS.any {
        ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED
    }

private fun safeVibrate(ctx: android.content.Context, millis: Long = 600) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = ctx.getSystemService(VibratorManager::class.java)
            vm?.defaultVibrator?.vibrate(
                VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            val vib = ctx.getSystemService(Vibrator::class.java)
            vib?.vibrate(
                VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    } catch (_: Throwable) {}
}

private fun safeBeep() {
    try {
        ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            .startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400)
    } catch (_: Throwable) {}
}

private fun openAppSettings(ctx: android.content.Context) {
    val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        .setData(android.net.Uri.fromParts("package", ctx.packageName, null))
    try { ctx.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) } catch (_: Throwable) {}
}

private fun isPermanentlyDenied(activity: Activity): Boolean =
    LOCATION_PERMS.any { p ->
        ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED &&
                !ActivityCompat.shouldShowRequestPermissionRationale(activity, p)
    }

/** ================= Pantalla principal ================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarDispositivoScreen(
    navController: NavController
) {
    val ctx = LocalContext.current

    // Factory para tu AndroidViewModel (usa Application)
    val vm: LocationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = ctx.applicationContext as Application
                @Suppress("UNCHECKED_CAST")
                return LocationViewModel(app) as T
            }
        }
    )

    val state by vm.state.collectAsState()

    // Verificar Google Play Services (evita crash si no hay GMS)
    val gmsOk by remember {
        mutableStateOf(
            GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(ctx) == ConnectionResult.SUCCESS
        )
    }

    var showRationale by remember { mutableStateOf(false) }
    var permanentlyDenied by remember { mutableStateOf(false) }

    val permReq = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { res ->
        val ok = res[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                res[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (ok) {
            showRationale = false
            permanentlyDenied = false
            vm.fetchLocation()
        } else {
            val act = ctx as? Activity
            permanentlyDenied = act?.let { isPermanentlyDenied(it) } ?: false
            showRationale = true
        }
    }

    ScreenScaffold(
        title = "Buscar dispositivo",
        canNavigateBack = true,
        onBack = { navController.popBackStack() },
        paragraphProvider = { listOf("Localiza el dispositivo, emite alerta y visualiza en el mapa.") }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /** ---- Acciones principales ---- **/
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { safeVibrate(ctx, 700); safeBeep() },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(Icons.Default.NotificationsActive, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Sonar / vibrar")
                }

                Button(
                    onClick = {
                        if (hasLocationPermission(ctx)) vm.fetchLocation()
                        else permReq.launch(LOCATION_PERMS)
                    },
                    modifier = Modifier.weight(1f).height(56.dp)
                ) {
                    Icon(Icons.Default.LocationSearching, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ubicar")
                }
            }

            /** ---- Rationale de permisos ---- **/
            if (showRationale) {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Necesitamos permiso de ubicación para ubicar el dispositivo.")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (permanentlyDenied) {
                                OutlinedButton(onClick = { openAppSettings(ctx) }) { Text("Abrir ajustes") }
                            } else {
                                OutlinedButton(onClick = { permReq.launch(LOCATION_PERMS) }) { Text("Conceder permiso") }
                            }
                        }
                    }
                }
            }

            /** ---- UI según estado ---- **/
            when (state) {
                is LocationUiState.Idle -> Text("Pulsa “Ubicar” para obtener la ubicación.")
                is LocationUiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
                is LocationUiState.Error -> Text(
                    (state as LocationUiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                is LocationUiState.Success -> {
                    val data = (state as LocationUiState.Success).data
                    val latLng = remember(data.lat, data.lon) { LatLng(data.lat, data.lon) }
                    val cameraState = rememberCameraPositionState()
                    val markerState = rememberMarkerState(position = latLng)

                    // Mover cámara una vez por actualización
                    LaunchedEffect(latLng) {
                        runCatching {
                            cameraState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                        }
                    }

                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Lat: ${"%.6f".format(Locale.US, data.lat)}  |  Lon: ${"%.6f".format(Locale.US, data.lon)}")
                            if (data.accuracy != null) Text("Precisión: ${data.accuracy} m")

                            // Mostrar mapa sólo si hay Google Play Services
                            if (gmsOk) {
                                Box(Modifier.fillMaxWidth().height(220.dp)) {
                                    GoogleMap(
                                        modifier = Modifier.fillMaxSize(),
                                        cameraPositionState = cameraState
                                    ) {
                                        Marker(
                                            state = markerState,
                                            title = "Mi teléfono",
                                            snippet = "Ubicación actual"
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    "Mapa no disponible (sin Google Play Services).",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val small = 48.dp

                                OutlinedButton(
                                    onClick = {
                                        val text = "Estoy aquí: https://maps.google.com/?q=${data.lat},${data.lon}"
                                        val i = Intent(Intent.ACTION_SEND)
                                            .setType("text/plain")
                                            .putExtra(Intent.EXTRA_TEXT, text)
                                        try { ctx.startActivity(Intent.createChooser(i, "Compartir ubicación")) } catch (_: Throwable) {}
                                    },
                                    modifier = Modifier.weight(1f).height(small)
                                ) {
                                    Icon(Icons.Default.Share, null); Spacer(Modifier.width(8.dp)); Text("Compartir")
                                }

                                OutlinedButton(
                                    onClick = {
                                        val geo = android.net.Uri.parse("geo:${data.lat},${data.lon}?q=${data.lat},${data.lon}(Mi+teléfono)")
                                        val intent = Intent(Intent.ACTION_VIEW, geo)
                                        try { ctx.startActivity(intent) } catch (_: Throwable) {}
                                    },
                                    modifier = Modifier.weight(1f).height(small)
                                ) {
                                    Icon(Icons.Default.Map, null); Spacer(Modifier.width(8.dp)); Text("Abrir en Maps")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
