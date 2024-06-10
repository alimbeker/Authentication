package relog.android.authentication.ui.theme.auth

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import relog.android.authentication.R
import relog.android.authentication.data.repository.AuthRepository
import relog.android.authentication.others.Resource
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val applicationContext: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _registerStatus = MutableStateFlow<Resource<AuthResult>?>(null)
    val registerStatus: StateFlow<Resource<AuthResult>?> = _registerStatus

    private val _loginStatus = MutableStateFlow<Resource<AuthResult>?>(null)
    val loginStatus: StateFlow<Resource<AuthResult>?> = _loginStatus

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            val error = applicationContext.getString(R.string.error_input_empty)
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar(error))
            }
        } else {
            _loginStatus.value = Resource.Loading()
            viewModelScope.launch(dispatcher) {
                val result = repository.login(email, password)
                _loginStatus.value = result
                if (result is Resource.Error) {
                    _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
                }
            }
        }
    }

    fun register(email: String, name: String, password: String) {
        val error = when {
            email.isEmpty() || name.isEmpty() || password.isEmpty() -> {
                applicationContext.getString(R.string.error_input_empty)
            }
            password.length < 8 -> {
                applicationContext.getString(R.string.error_password_short)
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                applicationContext.getString(R.string.error_invalid_email)
            }
            else -> null
        }

        error?.let {
            viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowSnackbar(it))
            }
            return
        }
        
        viewModelScope.launch(dispatcher) {
            val result = repository.register(email, name, password)
            _registerStatus.value = result
            if (result is Resource.Success) {
                _eventFlow.emit(UiEvent.NavigateToMain)
            } else if (result is Resource.Error) {
                _eventFlow.emit(UiEvent.ShowSnackbar(result.message ?: "Unknown error"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateToMain : UiEvent()
    }
}
