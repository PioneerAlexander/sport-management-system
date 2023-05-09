package graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import ru.emkn.kotlin.sms.*
import java.time.LocalTime

enum class TabState {
    GROUPS, PARTICIPANTS, DISTANCES, TEAMS, NOTEOFPARTICIPANTS
}

lateinit var mainCompetition: Competition

fun participantTableCreate(): MutableList<MutableList<MutableState<String>>> {
    val participantList = mutableListOf(
        mutableListOf(
            mutableStateOf("name"), mutableStateOf("surname"),
            mutableStateOf("ageGroup"),
            mutableStateOf("birthYear"),
            mutableStateOf("sportsCategory"),
            mutableStateOf("organisation"),
            mutableStateOf("startNumber"),
            mutableStateOf("hour"),
            mutableStateOf("minute"),
            mutableStateOf("second")
        )
    )
    mainCompetition.participants.forEach {
        participantList.add(
            mutableListOf(
                mutableStateOf(it.name),
                mutableStateOf(it.surname),
                mutableStateOf(it.ageGroup),
                mutableStateOf(it.birthYear),
                mutableStateOf(it.sportsCategory),
                mutableStateOf(it.organisation),
                mutableStateOf(it.startNumber),
                mutableStateOf(it.startTime.hour.toString()),
                mutableStateOf(it.startTime.minute.toString()),
                mutableStateOf(it.startTime.second.toString())
            )
        )
    }
    return participantList
}

fun splitsTableCreate(): MutableList<MutableList<MutableState<String>>> {
    var interiorList = mutableListOf(mutableStateOf("number"))
    val splitCopy = Input.splitsMap
    val splitsMaxSize = (splitCopy.values.map { it.size }).maxOrNull() ?: throw Exception("empty splits")
    for (i in 1..splitsMaxSize!!) {
        interiorList.add(mutableStateOf("name $i"))
        interiorList.add(mutableStateOf("time $i"))
    }
    val newSplitTable = splitCopy.toList()
    val splitsList = mutableListOf(interiorList)
    newSplitTable.forEach { curr ->
        interiorList = mutableListOf(mutableStateOf(curr.first))
        for (el in curr.second) {
            interiorList.add(mutableStateOf(el.name))
            interiorList.add(mutableStateOf(el.time.toString()))
        }
        splitsList.add(interiorList)
    }
    return splitsList
}

fun createSplitsMapFromTable() {
    val mapSplits = mutableMapOf<String, MutableList<MutableSplit>>()
    for (line in Tables.tableCreateSplits) {
        if (line[0].value != "number") {
            val key = line[0].value
            val valueMap = mutableListOf<MutableSplit>()
            for (i in 1 until line.size step 2) {
                val splittedTime = line[i + 1].value.split(":").map { it.toInt() }
                valueMap.add(MutableSplit(line[i].value, LocalTime.of(splittedTime[0], splittedTime[1], splittedTime[2])))
            }
            mapSplits[key] = valueMap
        }
    }
    Input.splitsMap = mapSplits
}

fun distanceTableCreate(): MutableList<MutableList<MutableState<String>>> {
    val splitsList = mutableListOf(mutableListOf(mutableStateOf("name"), mutableStateOf("distance")))

    csvReader().open(Files.courses.value.first()) {
        readAllWithHeaderAsSequence().forEach { row: Map<String, String> ->
            splitsList.add(
                mutableListOf(
                    mutableStateOf(checkMapElement(row["Название"])),
                    mutableStateOf(checkMapElement(row["Дистанция"])))
                )
        }
    }
    return splitsList
}


@Composable
fun ListsState(state: MutableState<State>): State {
    val tabs =
        listOf(
            Icons.Default.Home to State.ZERO,
            Icons.Rounded.List to TabState.DISTANCES,
            Icons.Filled.Person to TabState.PARTICIPANTS,
            Icons.Default.ShoppingCart to TabState.NOTEOFPARTICIPANTS
        )

    val tabState = remember { mutableStateOf(1) }
    Column {
        TabRow(selectedTabIndex = tabState.value, divider = {}, modifier = Modifier.height(50.dp)) {
            tabs.withIndex().forEach() { (index, tab) ->
                Tab(
                    icon = { Icon(tab.first, "") },
                    selected = tabState.value == index,
                    onClick = {
                        tabState.value = index
                    })
            }
        }
        when (tabState.value) {
            0 -> state.value = State.ZERO
            1 -> Table(Tables.tableCreateDistance).show(false)
            2 -> {
                Table(Tables.tableCreateParticipant).show(false)
            }
            3 -> {
                Table(Tables.tableCreateSplits).show(SaveOnSave = WhereToSave.SPLITS)
            }
            else -> {
            }
        }
    }


    return state.value
}

