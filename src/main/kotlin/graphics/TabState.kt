package graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class TabState {
    GROUPS, PARTICIPANTS, DISTANCES, TEAMS, NOTEOFPARTICIPANTS
}

@Composable
fun distributorTabState(state: MutableState<State>, enum: Enum<*>) {
    when (enum) {
        State.ZERO ->  state.value = State.ZERO
    }
}

@Composable
fun ListsState(state: MutableState<State>): State {
    val tabs =
        listOf(//Icons.Rounded.ArrowBack to State.ZERO,
            Icons.Default.Home to State.ZERO,
            Icons.Rounded.List to TabState.DISTANCES,
            Icons.Filled.Person to TabState.PARTICIPANTS,
            Icons.Rounded.AccountBox to TabState.GROUPS,
            Icons.Rounded.AddCircle to TabState.TEAMS,
            Icons.Rounded.FavoriteBorder to TabState.NOTEOFPARTICIPANTS
        )
    Column {
        val tabState = remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = tabState.value, divider = {}, modifier = Modifier.height(50.dp)) {
            tabs.withIndex().forEach() { (index, tab) ->
                Tab(
                    icon = { Icon(tab.first, "") },
                    selected = tabState.value == index,
                    onClick = {
                        tabState.value = index
                        if (tab.second == State.ZERO) {
                            state.value = State.ZERO
                        }
                    })
            }
        }
    }

    return state.value
}

