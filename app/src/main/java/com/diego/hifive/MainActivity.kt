package com.diego.hifive

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diego.hifive.models.DarkTheme
import com.diego.hifive.models.LocalTheme
import com.diego.hifive.screens.HomeScreen
import com.diego.hifive.screens.SettingsScreen
import com.diego.hifive.screens.WelcomeScreen
import com.diego.hifive.services.DataStoreManager
import com.diego.hifive.ui.theme.HiFiveTheme
import com.diego.hifive.viewmodel.HiViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = HiViewModel(this)


        setContent {
            val uiState by viewModel.uiState.collectAsState()
            HiFiveTheme(darkTheme = uiState.isDarkMode) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navController = rememberNavController()

                    lifecycleScope.launch {
                        val name = viewModel.dataStoreManager?.getName()?.first()
                        val theme = viewModel.dataStoreManager?.getTheme()?.first()

                        if (theme != null) {
                            viewModel.setThemeMode(theme == "true")
                        }

                        if (name != null) {
                            if (name.isEmpty()) {
                                navController.navigate("welcome")
                            } else {
                                viewModel.updateName(name)
                            }
                        }
                    }

                    NavHost(navController = navController, startDestination = "home") {
                        composable("welcome") { WelcomeScreen(viewModel = viewModel, navController) }
                        composable("home") { HomeScreen(viewModel = viewModel, navController) }
                        composable("settings") { SettingsScreen(viewModel = viewModel, navController) }
                    }

                }
            }
        }
    }
}