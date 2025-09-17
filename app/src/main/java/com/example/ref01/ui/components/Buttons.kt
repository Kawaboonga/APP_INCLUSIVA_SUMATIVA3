package com.example.ref01.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ref01.ui.utils.Dimens

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
    ),
    shape: RoundedCornerShape = RoundedCornerShape(50) // pill
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.ButtonHeight),   // 👈 misma altura en toda la app
        colors = colors,
        shape = shape
    ) {
        Text(text)
    }
}
