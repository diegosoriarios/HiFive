package com.diego.hifive.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diego.hifive.models.Message
import com.diego.hifive.models.STATES
import com.diego.hifive.services.DataStoreManager
import com.diego.hifive.services.WebSocketClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HiUIState(
    val name: String = "",
    val status: STATES = STATES.WAITING,
    val message: Message? = null,
    var isDarkMode: Boolean = false
)

class HiViewModel(context: Context): ViewModel() {
    private var webSocketClient: WebSocketClient = WebSocketClient(::updateMessage)
    private val _uiState = MutableStateFlow(HiUIState())
    val uiState: StateFlow<HiUIState> = _uiState.asStateFlow()
    var dataStoreManager: DataStoreManager? = null

    init {
        webSocketClient.start("ws://10.0.2.2:8080", uiState.value.name)
        dataStoreManager = DataStoreManager(context)
    }

    fun sendMessage(message: Message) {
        val jsonMessage = message.toJSON()
        webSocketClient.sendMessage(jsonMessage)
        _uiState.update { currentState ->
            currentState.copy(
                status = STATES.HANGING,
                message = Message("Waiting", "")
            )
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            dataStoreManager?.setName(name)
        }
        _uiState.update { currentState ->
            currentState.copy(
                name = name
            )
        }
    }

    fun updateMessage(message: String) {
        val messageReceived = Message().fromJSON(message)
        _uiState.update { currentState ->
            currentState.copy(
                message = messageReceived
            )
        }
    }

    fun resetStatus() {
        _uiState.update { currentState ->
            currentState.copy(
                status = STATES.WAITING,
                message = null
            )
        }
    }

    fun closeConnection() {
        webSocketClient.close()
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            dataStoreManager?.setTheme(!uiState.value.isDarkMode)
        }
        _uiState. update { currentState ->
            currentState.copy(
                isDarkMode = !currentState.isDarkMode
            )
        }
    }

    fun setThemeMode(theme: Boolean) {
        _uiState. update { currentState ->
            currentState.copy(
                isDarkMode = theme
            )
        }
    }
}