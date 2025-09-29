package com.timidgiraffe.chatmigo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun ChatScreen(chatViewModel: ChatViewModel = viewModel()) {
    var prompt by rememberSaveable { mutableStateOf("Enter prompt") }
    var result by rememberSaveable { mutableStateOf("result") }
    val uiState by chatViewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Chatmigo",
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            style = TextStyle(fontSize = 20.sp)
        )

        Row(modifier = Modifier.padding(15.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            TextField(
                value = prompt,
                modifier = Modifier.weight(1f),
                onValueChange = { prompt = it}
            )

            Spacer(modifier = Modifier.width(10.dp))

            Button (onClick = {
                chatViewModel.sendPrompt(prompt)
            }){
                Text("Go")
            }
        }

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if(uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }

            val scrollState = rememberScrollState()
            Text(modifier = Modifier
                .padding(15.dp)
                .verticalScroll(scrollState),
                color = textColor,
                style = TextStyle(fontSize = 15.sp),
                text = result,)
        }

    }

}

@Preview
@Composable
fun ChatScreenPreview() {
    ChatScreen()
}