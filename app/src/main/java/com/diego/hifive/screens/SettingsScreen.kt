package com.diego.hifive.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.diego.hifive.ui.theme.HiFiveTheme
import com.diego.hifive.viewmodel.HiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: HiViewModel, navController: NavController) {
    var text by remember { mutableStateOf(viewModel.uiState.value.name) }
    val uiState by viewModel.uiState.collectAsState()

    Column {
        Box(modifier = Modifier.padding(20.dp)) {
            Text(text = "Alterar nome", color = MaterialTheme.colorScheme.secondary)
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray,
                cursorColor = MaterialTheme.colorScheme.secondary,
                disabledLabelColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                textColor = Color.Black,
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Tema", color = MaterialTheme.colorScheme.secondary)
            Switch(
                checked = uiState.isDarkMode,
                onCheckedChange = {
                    viewModel.toggleDarkMode()
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Button(
            onClick = {
                viewModel.updateName(text)
                navController.popBackStack()
            },
            enabled = text.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary, disabledContainerColor = Color.LightGray)
        ) {
            Text(text = "Salvar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    HiFiveTheme {
        SettingsScreen(viewModel = HiViewModel(LocalContext.current), navController = NavController(LocalContext.current))
    }
}