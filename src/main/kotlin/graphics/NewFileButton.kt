package graphics

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import java.awt.FileDialog
import java.io.File


@Composable
fun NewFileButton(text: String, modifier: Modifier, onClick: () -> Unit = {}, type: InputFilesType) {
    val fileDialog = FileDialog(ComposeWindow())

    val files = mutableListOf<File>()
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128)),
        onClick = {
            fileDialog.isMultipleMode = true
            fileDialog.isVisible = true
            files += fileDialog.files
            when (type) {
                InputFilesType.EVENT -> Files.event.value = files
                InputFilesType.APPLICATIONS -> Files.applications.value = files
                InputFilesType.SAVED -> Files.saved.value = files
                InputFilesType.COURSES -> Files.courses.value = files
                InputFilesType.CLASSES -> Files.classes.value = files
                InputFilesType.SPLITS -> Files.splits.value = files
                else -> {

                }
            }
            onClick()
        }
    ) {
        Text(text, color = Color.White)
    }
}
