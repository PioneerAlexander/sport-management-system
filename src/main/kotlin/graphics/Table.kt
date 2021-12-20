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
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)

class Table(val composeTable: MutableList<MutableList<MutableState<String>>>) {
    val composeTableSize = mutableStateOf(composeTable.size)

    @Composable
    fun show(mutable: Boolean) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            repeat(composeTableSize.value) { index ->
                val readOnly = if (mutable) index == 0 else true
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    for ((rowIndex, field) in composeTable[index].withIndex()) {
                        TextField(
                            modifier = Modifier.onPreviewKeyEvent {
                                (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
                            }.width(150.dp),
                            value = field.value,
                            onValueChange = {
                                composeTable[index][rowIndex].value = it
                            },
                            placeholder = { Text("Значение.") },
                            readOnly = readOnly,
                            singleLine = true
                        )
                    }
                    AddButton {
                        if (mutable) {
                            composeTable.add(index + 1, MutableList(composeTable[0].size) { mutableStateOf("") })
                            composeTableSize.value += 1
                        }
                    }
                    if (index != 0) {
                        DeleteButton {
                            if (mutable) {
                                composeTable.removeAt(index)
                                composeTableSize.value -= 1
                            }
                        }
                    } else SaveButton {
                        //переход к какому-то другому окну, функции, запуск программы дальше
                    }
                }

            }
        }
    }

    @Composable
    private fun AddButton(onClick: () -> Unit) {
        IconButton(
            onClick = onClick
        ) { Icon(Icons.Default.Add, "") }

    }

    @Composable
    private fun DeleteButton(onClick: () -> Unit) {
        IconButton(
            onClick = onClick
        ) { Icon(Icons.Default.Delete, "") }
    }

    @Composable
    private fun SaveButton(onClick: () -> Unit) {
        IconButton(
            onClick = onClick
        ) { Icon(Icons.Default.Done, "") }
    }

}




