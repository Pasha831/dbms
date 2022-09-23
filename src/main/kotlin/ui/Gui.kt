package ui

import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import data.StudentsTable

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
        // TODO: make it a mutableStateListOf<Student>()
        val tableData = StudentsTable.studentsList

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
                                onClick = {},
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }

    fun launchScreen() = application {
        Window(onCloseRequest = ::exitApplication) {
            App()
        }
    }
}