package relog.android.authentication.ui.theme.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.tooling.preview.Preview
import relog.android.authentication.ui.theme.MainTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@AndroidEntryPoint
class MainActivity : ComponentActivity()


@Composable
fun MainScreen(navController: NavHostController, onLogoutClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello World!")

        Button(
            onClick = {
                onLogoutClick()
                navController.navigate("login") // Navigate to the login screen after logout
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA000D)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainTheme {
        MainScreen(navController = rememberNavController())
    }
}


