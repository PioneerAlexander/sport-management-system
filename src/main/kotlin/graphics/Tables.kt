package graphics

import androidx.compose.runtime.mutableStateOf

object Tables {
    var tableCreateParticipant = mutableListOf(mutableListOf(mutableStateOf("")))
    var tableCreateSplits = mutableListOf(mutableListOf(mutableStateOf("")))
    var tableCreateDistance = mutableListOf(mutableListOf(mutableStateOf("")))
}