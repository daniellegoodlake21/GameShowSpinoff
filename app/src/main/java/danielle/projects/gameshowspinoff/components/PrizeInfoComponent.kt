package danielle.projects.gameshowspinoff.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import danielle.projects.gameshowspinoff.model.Prize
import danielle.projects.gameshowspinoff.screen.PrizeBuilderViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrizeInfoComponent(prizeBuilderViewModel: PrizeBuilderViewModel, prize: Prize?) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val input = rememberSaveable{
        mutableStateOf(prize?.prizeTitle?: "")
    }
    Card(colors = CardDefaults.cardColors(containerColor = Color.LightGray), modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            if (prize != null) {
                OutlinedTextField(
                    value = input.value,
                    onValueChange = { newText ->
                        input.value = newText
                        prizeBuilderViewModel.updatePrize(prize = prize.copy(prizeTitle = input.value))

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onSend = { keyboardController?.hide() },
                        onDone = { keyboardController?.hide()},
                        onNext = { keyboardController?.hide()})
                )

                Spacer(modifier = Modifier.padding(12.dp))
                Button(onClick = { prizeBuilderViewModel.removePrize(prize) }) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete Prize",
                        Modifier
                            .padding(12.dp)
                            .size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(12.dp))
            }
        }
    }
}