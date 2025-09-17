// ui/components/AppTopBar.kt
package com.example.ref01.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ref01.core.tts.LocalReadAloud



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    canNavigateBack: Boolean,
    onBack: (() -> Unit)?,
    paragraphProvider: () -> List<String>,
    containerColor: Color,
    contentColor: Color = Color.White,
    iconSize: Dp = 28.dp,
    onDecFont: (() -> Unit)? = null,
    onIncFont: (() -> Unit)? = null,
    showTitle: Boolean = true,
    showBackLabel: Boolean = false,
    backLabel: String = "Volver",
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val reader = LocalReadAloud.current

    TopAppBar(
        title = {
            if (showTitle) {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 20.dp)
                )
            } else {
                Text(text = "")
            }
        },
        navigationIcon = {
            if (canNavigateBack && onBack != null) {
                if (showBackLabel) {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(start = 8.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            modifier = Modifier.size(iconSize),
                            tint = contentColor
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = backLabel,
                            color = contentColor,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            modifier = Modifier.size(iconSize),
                            tint = contentColor
                        )
                    }
                }
            }
        },
        actions = {
            // PLAY: inicia cola si hay texto; si no, intenta reanudar
            IconButton(onClick = {
                val ps = paragraphProvider()
                if (ps.isNotEmpty()) reader.readParagraphs(ps) else reader.resume()
            }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Leer/Reanudar",
                    modifier = Modifier.size(iconSize),
                    tint = contentColor
                )
            }

            // PAUSE
            IconButton(onClick = { reader.pause() }) {
                Icon(
                    imageVector = Icons.Filled.Pause,
                    contentDescription = "Pausar",
                    modifier = Modifier.size(iconSize),
                    tint = contentColor
                )
            }

            // STOP
            IconButton(onClick = { reader.stop() }) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "Detener",
                    modifier = Modifier.size(iconSize),
                    tint = contentColor
                )
            }

            // A− / A+
            onDecFont?.let {
                TextButton(onClick = it) { Text("A−", color = contentColor) }
            }
            onIncFont?.let {
                TextButton(onClick = it) { Text("A+", color = contentColor) }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        scrollBehavior = scrollBehavior
    )
}
