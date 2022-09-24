package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import data.StudentsTable
import java.util.Timer
import kotlin.concurrent.schedule

class Gui {
    @Composable
    private fun RowScope.TableCell(
        text: String,
        weight: Float
    ) {
        Text(
            text = text,
            Modifier
                .border(1.dp, Color.Black)
                .weight(weight)
                .padding(8.dp)
        )
    }

    @Composable
    private fun StudentsTableScreen() {
        val tableData by mutableStateOf(StudentsTable.studentsList)

        val column1Weight = .25f // 25%
        val column2Weight = .25f // 25%
        val column3Weight = .25f // 25%
        val column4Weight = .25f // 25%

        LazyColumn(
            Modifier
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp)
                .border(2.dp, Color.Black)
        ) {
            // Here is the header
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "ID", weight = column1Weight)
                    TableCell(text = "Name", weight = column2Weight)
                    TableCell(text = "Surname", weight = column3Weight)
                    TableCell(text = "Patronymic", weight = column4Weight)
                }
            }

            // Here are all the lines of the table.
            items(tableData) {
                val (id, firstname, lastname, patronymic) = it
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = id.toString(), weight = column1Weight)
                    TableCell(text = firstname, weight = column2Weight)
                    TableCell(text = lastname, weight = column3Weight)
                    TableCell(text = patronymic, weight = column4Weight)
                }
            }
        }
    }

    @Composable
    @Preview
    fun App() {
        var isStudentTableVisible by remember { mutableStateOf(false) }
        var isDialogOpen by remember { mutableStateOf(false) }
        var dialogOperation by remember { mutableStateOf("") }

        MaterialTheme {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 8.dp,
                            bottom = 16.dp
                        ),
                        text = "Tables:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                    Row {
                        Button({ isStudentTableVisible = !isStudentTableVisible }) {
                            Text("Students")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button({ }) {
                            Text("Variants")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button({ }) {
                            Text("Testing")
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(modifier = Modifier.weight(0.85f)) {
                        if (isStudentTableVisible) {
                            StudentsTableScreen()
                        }
                    }
                    Row(modifier = Modifier.weight(0.15f)) {
                        if (isStudentTableVisible) {
                            Button(
                                onClick = {
                                    isDialogOpen = true
                                    dialogOperation = "add"
                                },
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Add")
                            }

                            if (isDialogOpen) {
                                if (dialogOperation == "add") {
                                    openAddDialog(onOpenDialog = { isDialogOpen = false })
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun openAddDialog(onOpenDialog: () -> Unit) {
        var newFirstname by remember { mutableStateOf("") }
        var newLastname by remember { mutableStateOf("") }
        var newPatronymic by remember { mutableStateOf("") }

        var isOperationSuccessful by remember { mutableStateOf(false) }
        var isMessageVisible by remember { mutableStateOf(false) }

        val onAddClick:() -> Unit = {
            if (newFirstname.isEmpty() || newLastname.isEmpty()) {
                isOperationSuccessful = false
            } else {
                StudentsTable.studentsList.add(
                    StudentsTable.Student(
                        StudentsTable.currentId++,
                        firstname = newFirstname,
                        lastname = newLastname,
                        patronymic = newPatronymic.ifEmpty { "-" }
                    )
                )
                isOperationSuccessful = true
            }

            isMessageVisible = true
            Timer().schedule(2000) {
                isMessageVisible = false
            }
        }

        Dialog(
            title = "New person",
            onCloseRequest = onOpenDialog,
            state = rememberDialogState(position = WindowPosition(Alignment.Center))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().width(250.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = newFirstname,
                    onValueChange = { newFirstname = it },
                    label = { Text("First name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newLastname,
                    onValueChange = { newLastname = it },
                    label = { Text("Last name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = newPatronymic,
                    onValueChange = { newPatronymic = it },
                    label = { Text("Patronymic (if you have it)") },
                    singleLine = true
                )
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text("Add new person")
                }

                AnimatedVisibility(
                    visible = isMessageVisible,
                ) {
                    if (isOperationSuccessful) {
                        Text(
                            "New person added!",
                            color = Color(0xFF33CC00)
                        )
                    } else {
                        Text(
                            "Person must have both first and last name!",
                            color = Color(0xFFFF0000)
                        )
                    }
                }
            }
        }
    }

    fun launchMainScreen() = application {
        Window(onCloseRequest = ::exitApplication) {
            App()
        }
    }
}