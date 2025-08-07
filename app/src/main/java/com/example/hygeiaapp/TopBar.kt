package com.example.hygeiaapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.hygeiaapp.ui.theme.PrimaryBlue
import com.example.hygeiaapp.ui.theme.SecondaryBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar() {
    TopAppBar(
        title = {
            Text("Home", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO: Open Drawer */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            IconButton(onClick = { /* TODO: Open Profile */ }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = SecondaryBlue)
    )
}

