package graphics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.sp
import ru.emkn.kotlin.sms.startOnCl

@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun StartPr(state: MutableState<State>, data: List<List<String>>): State {

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(10.dp)
    ) {
        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ) {
            Column {
                data.forEach { listS ->
                    Row {
                        listS.forEach {
                            Text(
                                text = "$it,",
                                color = Color.Black,
                                fontSize = 20.sp
                            )
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
        Button(onClick = { state.value = State.ZERO }, modifier = Modifier.align(Alignment.TopEnd)) {
            Text(
                text = "x",
                color = Color.White
            )
        }

    }

    return state.value
}

@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun PrintDock(state: MutableState<State>): State {

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.White)
            .padding(10.dp)
    ) {
        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ) {
            Column {
                Text(text = "# Электронная система проведения соревнований\n" +
                        "\n" +
                        "# Использование программы\n" +
                        "\n" +
                        "Есть два сценария работы с программой:\n" +
                        "* Обработка заявок и создание стартовых протоколов\n" +
                        "* Генерация результатов соревнований по данным о прохождении контрольных точек. Последнее можно использовать только после\n" +
                        "  создания соответствующего протокола старта\n" +
                        "\n" +
                        "# Немного о формате файлов\n" +
                        "\n" +
                        "* Пример файла соревнования (CSV):\n" +
                        "\n" +
                        "    ```csv\n" +
                        "    Название,Дата\n" +
                        "    Первенство пятой бани,01.01.2022\n" +
                        "    ```\n" +
                        "\n" +
                        "* Пример заявочного списка (CSV):\n" +
                        "\n" +
                        "    ```csv\n" +
                        "    Выборгский СДЮШСОР №10,,,,,,,\n" +
                        "    Группа,Фамилия,Имя,Г.р.,Разр.\n" +
                        "    Иванов,Иван,2002,КМС,М21,,,\n" +
                        "    Петров,Пётр,1978,I,М40,,,  \n" +
                        "    Пупкин,Василий,2011,3ю,М10,,\n" +
                        "    ```\n" +
                        "\n" +
                        "* Пример протокола прохождения дистанции участником (CSV):\n" +
                        "\n" +
                        "    ```csv\n" +
                        "    Participant,243,\n" +
                        "    1km,12:06:15\n" +
                        "    2km,12:10:36\n" +
                        "    Finish,12:14:51\n" +
                        "    ```\n" +
                        "\n" +
                        "* Пример протокола прохождения контрольного пункта (CSV):\n" +
                        "\n" +
                        "    ```csv\n" +
                        "    Checkpoint,1km,\n" +
                        "    241,12:04:17\n" +
                        "    242,12:05:11\n" +
                        "    243,12:06:15\n" +
                        "    ```\n" +
                        "\n" +
                        "* Пример протокола соответствия категорий маршрутам (CSV):\n" +
                        "\n" +
                        "    ```csv\n" +
                        "    Название,Дистанция\n" +
                        "    М10,МЖ9 10\n" +
                        "    М12,М12\n" +
                        "    М14,М14\n" +
                        "    М16,М16 Ж60\n" +
                        "    ```\n" +
                        "* Пример протокола маршрутов (CSV) - последняя контрольная точка считается финишем:\n" +
                        "\n" +
                        "    ```csv\n" +
                        "    Название,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25\n" +
                        "    МЖ9 10,32,46,34,33,ЛюбоеСлово,,,,,,,,,,,,,,,,,,,,\n" +
                        "    Ж14,47,46,45,34,33,32,48,52,51,50,49,53,,,,,,,,,,,,,\n" +
                        "    Ж12,32,46,33,47,48,52,51,50,49,53,,,,,,,,,,,,,,,\n" +
                        "    М12,32,46,34,35,45,33,47,48,52,49,53,,,,,,,,,,,,,,\n" +
                        "    SUPER,21,!w22 w23,~2 24t 25t 26t 27t 28t,29,,,,,,,,,,,,,,,,,,,,,\n" +
                        "    ```\n" +
                        "Синтаксис для задачи дистанций:\n" +
                        "* В названиях контрольных пунктов нельзя использовать восклицательный знак(!), тильду(~), запятую(,), пробел( ).\n" +
                        "* Блоки маршрута отделяются друг от друга запятыми и должны проходиться именно в написанном порядке.\n" +
                        "* Блок состоящий из одного названия контрольного пункта (,10, или ,ЛюбоеСлово,) значит, что участник должен посетить этот контрольный пункт.\n" +
                        "* Блок состоящий из восклицательного знака(!) за которым идут через пробел названия контрольных пунктов значит, что участник должен посетить ровно один из указанных пунктов на выбор (,!w22 w23, значит можно на выбор посетить либо w22, либо w22)\n" +
                        "* Блок состоящий из тильды(~) за которой идет натуральное число, а далее через пробел названия контрольных пунктов значит, что участник должен посетить ровно указанное число различных записанных пунктов на выбор в любом порядке(,~2 24t 25t 26t 27t 28t, значит, что подходят любые два пункта в любом порядке на выбор участника, к примеру (25t потом 27t) или (27t потом 25t), или (24t потом 28t).\n" +
                        "\n" +
                        "Желаем вам успешного использования программы. Читайте сообщения об ошибках - в них кроется информация о том, что именно пошло не так в ходе работы с вашими данными.\n" +
                        "Возможно, их потребуется поменять для корректной работы программы.\n",
                        color = Color(100, 0, 100),
                        fontSize = 16.sp

                )

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
        Button(onClick = { state.value = State.ZERO }, modifier = Modifier.align(Alignment.TopEnd)) {
            Text(
                text = "x",
                color = Color.White
            )
        }

    }

    return state.value
}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImportState(state: MutableState<State>): State {
    Button(onClick = { state.value = State.ZERO }) { Text(text = "Назад", color = Color.White) }
    Column(modifier = Modifier.fillMaxWidth().offset(0.dp, 100.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {

        TextField(
            modifier = Modifier.onPreviewKeyEvent {
                (it.key == Key.Enter)         // чтобы нельзя было перенести строку при вводе
            }.align(Alignment.CenterHorizontally),
            value = Files.directory.value,
            onValueChange = { Files.directory.value = it },
            placeholder = { Text("Имя папки для сохранения.") }
        )
        NewFileButton("Файл соревнования", Modifier.align(Alignment.CenterHorizontally).width(300.dp), {
            Files.gotEvent.value = true
        }, type = InputFilesType.EVENT)
        NewFileButton(
            "Все файлы заявок",
            Modifier.align(Alignment.CenterHorizontally).width(300.dp),
            {
                Files.gotApplications.value = true
            },
            type = InputFilesType.APPLICATIONS
        )
        if (Files.loadingSaved.value || (Files.gotApplications.value && (Files.gotEvent.value))) {
            if (Files.isStartProtMade.value){
                Files.startList = mutableStateOf(startOnCl(Files.event.value.first(), Files.applications.value, Files.directory.value))
                Files.isStartProtMade = mutableStateOf(false)
            }
            Button(
                onClick = {
                    state.value = State.START_PROTOCOLS
                },
                modifier = Modifier.align(Alignment.CenterHorizontally).width(300.dp).height(60.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(128, 0, 128))
            ) {
                Text(
                    text = "Стартовые протоколы",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
    return state.value

}