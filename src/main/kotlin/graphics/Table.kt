package graphics

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import ru.emkn.kotlin.sms.startOnCl
import androidx.compose.foundation.HorizontalScrollbar as HorizontalScrollbar

@OptIn(ExperimentalComposeUiApi::class)

class Table(val composeTable: MutableList<MutableList<MutableState<String>>>) {
    val composeTableSize = mutableStateOf(composeTable.size)

    @Composable
    fun show(mutable: Boolean = true, onSave: ()-> Unit = {}) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = Color(251, 206, 177))
                .padding(10.dp)
        ) {
            val stateVertical = rememberScrollState(0)
            val stateHorizontal = rememberScrollState(0)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(stateVertical)
                    .padding(end = 12.dp, bottom = 12.dp)
                    .horizontalScroll(stateHorizontal).align(Alignment.Center)
            ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                repeat(composeTableSize.value) { index ->

                    val readOnly = if (mutable) index == 0 else true
                    Row(
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
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
                        } else {
                            SaveButton { onSave() }
                        }
                    }
                }
            }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(stateVertical)
            )
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                adapter = rememberScrollbarAdapter(stateHorizontal)
            )
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




