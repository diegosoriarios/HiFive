package com.diego.hifive.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diego.hifive.R
import com.diego.hifive.components.GifImage
import com.diego.hifive.models.Message
import com.diego.hifive.models.STATES
import com.diego.hifive.ui.theme.HiFiveTheme
import com.diego.hifive.viewmodel.HiViewModel

@Composable
fun HomeScreen(viewModel: HiViewModel, navController: NavController) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (uiState.status == STATES.HIGHFIVED) {
                    viewModel.resetStatus()
                } else if (uiState.status == STATES.WAITING) {
                    val message = Message(text = "HANGING", sender = uiState.name)
                    viewModel.sendMessage(message)
                }
            }) {
        Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
            if (uiState.message != null) {
                Text(uiState.message!!.text, color = MaterialTheme.colorScheme.secondary)
            } else {
                Text("Click to send a High Five", color = MaterialTheme.colorScheme.secondary)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Gif(uiState.status, uiState.isDarkMode)
            }
            Box(modifier = Modifier.height(94.dp)) {
                SettingsButton(navController = navController)
            }
        }
    }
}

@Composable
fun Gif(state: STATES, isDark: Boolean) {
    when (state) {
        STATES.WAITING -> {
            val icon = if (isDark) R.drawable.touch_dark else R.drawable.touch
            GifImage(icon = icon, modifier = Modifier.height(400.dp).width(400.dp))
        }
        STATES.HANGING -> {
            val icon = if (isDark) R.drawable.waiting_dark else R.drawable.waiting
            GifImage(icon = icon, modifier = Modifier.height(400.dp).width(400.dp))
        }
        STATES.HIGHFIVED -> {
            val icon = if (isDark) R.drawable.high_five_dark else R.drawable.high_five
            GifImage(icon = icon, modifier = Modifier.height(400.dp).width(400.dp))
        }
    }
}

@Composable
fun SettingsButton(navController: NavController) {
    IconButton(
        onClick = {
            navController.navigate(route = "settings")
        }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HiFiveTheme {
        HomeScreen(viewModel = HiViewModel(LocalContext.current), navController = NavController(LocalContext.current))
    }
}