package graphics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)

class Table(table: MutableList<MutableList<String>>) {
    val composeTable = mutableStateOf(table)
    @Composable
    fun show() {
        Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.spacedBy(5.dp)) {
            for ((index, row) in composeTable.value.withIndex()) {

                val enabled = index != 0
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    for (rowIndex in row.indices) {
                        TextField(
                            modifier = Modifier.onPreviewKeyEvent {
                                (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
                            }.width(50.dp),
                            value = composeTable.value[index][rowIndex],
                            onValueChange = { composeTable.value[index][rowIndex] = it },
                            placeholder = { Text("Значение.") },
                            enabled = enabled
                        )
                    }
                    AddButton(index)
                    if (enabled) DeleteButton(index)
                }

            }
        }
    }

    @Composable
    private fun AddButton(index: Int) {
        IconButton(onClick = {
            composeTable.value.add(index, MutableList(composeTable.value[0].size) { "1" })
            println(
                composeTable.value
            )
        }) { Icon(Icons.Default.Add, "") }

    }

    @Composable
    private fun DeleteButton(index: Int) {
        IconButton(onClick = { composeTable.value.removeAt(index) }) { Icon(Icons.Default.Delete, "") }
    }
}




