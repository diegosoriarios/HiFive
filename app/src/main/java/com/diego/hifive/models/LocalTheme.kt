package com.diego.hifive.models

import androidx.compose.runtime.compositionLocalOf

data class DarkTheme(val isDark: Boolean = false)

val LocalTheme = compositionLocalOf { DarkTheme() }