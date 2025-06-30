package com.example.fridgemate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF1EB980),
    secondary = androidx.compose.ui.graphics.Color(0xFF045D56),
    background = androidx.compose.ui.graphics.Color(0xFF26282F)
)

@Composable
fun FridgeMateTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
