package com.example.ref01.ui.components

import androidx.annotation.RawRes
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.example.ref01.R
import com.example.ref01.ui.theme.Ref01Theme

@Composable
fun DrawScreen(
    @RawRes rawRes: Int,
    modifier: Modifier = Modifier,
    height: Dp = 220.dp,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Animación" },
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
        )
    }
}

