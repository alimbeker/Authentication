package relog.android.authentication.ui.theme.auth.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import relog.android.authentication.others.Resource
import com.google.firebase.auth.AuthResult
import relog.android.authentication.ui.theme.auth.AuthViewModel
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val registerStatus by viewModel.registerStatus.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AuthViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(event.message)
                    }
                }
                is AuthViewModel.UiEvent.NavigateToMain -> {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(title = { Text("Create Account") })
        },
        content = { innerPadding ->
            RegisterContent(
                viewModel = viewModel,
                navController = navController,
                registerStatus = registerStatus,
                contentPadding = innerPadding
            )
        }
    )
}

@Composable
fun RegisterContent(
    viewModel: AuthViewModel,
    navController: NavController,
    registerStatus: Resource<AuthResult>?,
    contentPadding: PaddingValues
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading = registerStatus is Resource.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { /* Handle password visibility toggle */ }) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "Toggle password visibility")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.register(email.trim(), name.trim(), password.trim())
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text("Already have an account? Login Now")
        }
    }
}