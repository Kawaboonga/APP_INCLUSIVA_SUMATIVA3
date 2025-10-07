package com.example.ref01.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.Locale

data class LocationAddress(val lat: Double, val lon: Double, val accuracy: Float?, val address: String?, val time: Long)

class LocationRepository(private val context: Context) {
    private val fused by lazy { LocationServices.getFusedLocationProviderClient(context) }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationAddress? {
        val cts = CancellationTokenSource()
        val loc: Location? = try {
            fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token).await()
        } catch (_: Exception) { null }

        val best = loc ?: try { fused.lastLocation.await() } catch (_: Exception) { null }
        best ?: return null

        val addr = runCatching {
            val gc = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            gc.getFromLocation(best.latitude, best.longitude, 1)?.firstOrNull()?.let {
                listOfNotNull(it.thoroughfare, it.subThoroughfare, it.locality, it.adminArea, it.countryName)
                    .joinToString(", ")
            }
        }.getOrNull()

        return LocationAddress(
            lat = best.latitude,
            lon = best.longitude,
            accuracy = best.accuracy,
            address = addr,
            time = best.time
        )
    }

    @Suppress("MissingPermission")
    suspend fun getLastKnownLocation(context: Context): Result<Pair<Double, Double>> = runCatching {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        val loc = suspendCancellableCoroutine<Location?> { cont ->
            fused.lastLocation
                .addOnSuccessListener { cont.resume(it) {} }
                .addOnFailureListener { cont.resume(null) {} }
        } ?: throw IllegalStateException("Sin ubicación disponible")
        loc.latitude to loc.longitude
    }


}
