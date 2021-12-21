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
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import myDB.CompetitionsT
import myDB.ParticipantsT
import myDB.recreateCompetition
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.emkn.kotlin.sms.*

enum class TabState {
    GROUPS, PARTICIPANTS, DISTANCES, TEAMS, NOTEOFPARTICIPANTS
}

lateinit var compeTition: Competition

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
    logger.info { "Вы здесь" }
    compeTition.participants.forEach {
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
    logger.info { "После" }
    return participantList
}

fun splitsTableCreate(): MutableList<MutableList<MutableState<String>>> {
    var interiorList = mutableListOf(mutableStateOf("number"))
    val splitCopy = Input.splitsMap
    val splitsMaxSize = (splitCopy.values.map { it.size }).maxOrNull() ?: throw Exception("bitch")
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
        listOf(//Icons.Rounded.ArrowBack to State.ZERO,
            Icons.Rounded.List to TabState.DISTANCES,
            Icons.Default.Home to State.ZERO,
            Icons.Filled.Person to TabState.PARTICIPANTS,
            Icons.Rounded.AccountBox to TabState.GROUPS,
            Icons.Rounded.AddCircle to TabState.TEAMS,
            Icons.Rounded.FavoriteBorder to TabState.NOTEOFPARTICIPANTS
        )

    val tabState = remember { mutableStateOf(0) }
    Column {
        TabRow(selectedTabIndex = tabState.value, divider = {}, modifier = Modifier.height(50.dp)) {
            tabs.withIndex().forEach() { (index, tab) ->
                Tab(
                    icon = { Icon(tab.first, "") },
                    selected = tabState.value == index,
                    onClick = {
                        tabState.value = index
                    })
                logger.info { "тут" }

            }
        }
        when (tabState.value) {
            0 -> Table(Tables.tableCreateDistance).show(false)
            1 -> state.value = State.ZERO
            2 -> {
                logger.info { "uq" }
                Table(Tables.tableCreateParticipant).show(false)
            }
            5 -> {
                Table(Tables.tableCreateSplits).show()
            }
            else -> {
            }
        }
    }


    return state.value
}

