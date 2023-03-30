package com.example.fetch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.example.fetch.models.Item
import com.example.fetch.ui.theme.FetchTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetch.network.NetworkManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FetchTheme {
                MainScreen()
            }
        }
    }
}
@Composable
fun MainScreen() {
    var items by remember {
        mutableStateOf(
            listOf(
                Item(
                    0,
                    0, "Loading..."
                )
            )
        )
    }
    LaunchedEffect(true) {
        fetch { items = it }
    }
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Row {
            Conversation(
                items
            )
        }
    }
}

fun fetch(callback: (items: List<Item>) -> Unit) {
    val networkManager = NetworkManager()
    networkManager.fetch(callback)
}

@Composable
fun ItemCard(item: Item) {
    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp).fillMaxWidth()) {

        Column(modifier = Modifier.background(Color.White).fillMaxSize()) {
            Text(item.name.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "ID: " + item.id.toString(), fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "List ID: " + item.listId.toString(), fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@Composable
fun Conversation(messages: List<Item>) {
    LazyColumn {
        items(messages) { message ->
            ItemCard(message)
        }
    }
}
