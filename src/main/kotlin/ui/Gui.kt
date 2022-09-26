package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import data.StudentsTable
import data.TestingTable
import data.VariantsTable
import java.util.Timer
import kotlin.concurrent.schedule

class Gui {
    private var isStudentTableVisible by mutableStateOf(true)  // starting table
    private var isVariantsTableVisible by mutableStateOf(false)
    private var isTestingTableVisible by mutableStateOf(false)

    private var isDialogOpen by mutableStateOf(false)
    private var dialogOperation by mutableStateOf("")

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
    private fun RowScope.TableDeleteCell(
        text: String,
        weight: Float,
        onClick: () -> Unit
    ) {
        Text(
            text = text,
            Modifier
                .border(1.dp, Color.Black)
                .weight(weight)
                .padding(8.dp)
                .clickable(onClick = onClick),
            textAlign = TextAlign.Center,
            color = Color.Red
        )
    }

    private fun <T> SnapshotStateList<T>.swapList(newList: MutableList<T>) {
        clear()
        addAll(newList)
    }

    @Composable
    private fun StudentsTableScreen() {
        val tableData = mutableStateListOf<StudentsTable.Student>()
        tableData.swapList(StudentsTable.studentsList)

        val column1Weight = .1f // 25%
        val column2Weight = .25f // 25%
        val column3Weight = .25f // 25%
        val column4Weight = .25f // 25%
        val column5Weight = .1f

        LazyColumn(
            Modifier.border(4.dp, Color.Black)
        ) {
            // Here is the header
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "ID", weight = column1Weight)
                    TableCell(text = "Name", weight = column2Weight)
                    TableCell(text = "Surname", weight = column3Weight)
                    TableCell(text = "Patronymic", weight = column4Weight)
                    TableCell(text = "", weight = column5Weight)
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
                    TableDeleteCell(
                        text = "Delete",
                        weight = column5Weight,
                        onClick = {
                            StudentsTable.studentsList.removeIf { student -> student.id == id }
                            StudentsTable.refresh()
                            tableData.swapList(StudentsTable.studentsList)
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun VariantsTableScreen() {
        val tableData by mutableStateOf(VariantsTable.variantsList)

        val column1Weight = .1f
        val column2Weight = .9f

        LazyColumn(
            Modifier.border(4.dp, Color.Black)
        ) {
            // Here is the header
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "ID", weight = column1Weight)
                    TableCell(text = "Variant", weight = column2Weight)
                }
            }

            // Here are all the lines of the table.
            items(tableData) {
                val (id, name) = it
                Row(Modifier.fillMaxWidth()) {
                    TableCell(text = id.toString(), weight = column1Weight)
                    TableCell(text = name, weight = column2Weight)
                }
            }
        }
    }

    @Composable
    private fun TestingTableScreen() {
        val tableData by mutableStateOf(TestingTable.testingList)

        val column1Weight = .8f // 25%
        val column2Weight = .2f // 25%

        LazyColumn(
            Modifier.border(4.dp, Color.Black)
        ) {
            // Here is the header
            item {
                Row(Modifier.background(Color.Gray)) {
                    TableCell(text = "Full name", weight = column1Weight)
                    TableCell(text = "Variant", weight = column2Weight)
                }
            }

            // Here are all the lines of the table.
            items(tableData) {
                val (_, _, fullName, variant) = it
                Row(Modifier.fillMaxWidth()) {
                    TableCell(
                        text = fullName,
                        weight = column1Weight
                    )
                    TableCell(
                        text = variant,
                        weight = column2Weight
                    )
                }
            }
        }
    }

    @Composable
    private fun addInstrumentationButtons(
        add: Boolean = false,
        find: Boolean = false
    ) {
        if (add) {
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    isDialogOpen = true
                    dialogOperation = "add"
                },
            ) {
                Text("Add")
            }
        }
        if (find) {
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    isDialogOpen = true
                    dialogOperation = "find"
                },
            ) {
                Text("Find")
            }
        }
    }

    private fun switchTablesVisibility(
        students: Boolean = false,
        variants: Boolean = false,
        testing: Boolean = false
    ) {
        isStudentTableVisible = students
        isVariantsTableVisible = variants
        isTestingTableVisible = testing
    }

    @Composable
    @Preview
    fun App() {
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .weight(0.1f)
                        .fillMaxWidth()
                ) {
                    OutlinedButton({ switchTablesVisibility(students = true) }) {
                        Text("Students")
                    }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton({ switchTablesVisibility(variants = true) }) {
                        Text("Variants")
                    }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton({ switchTablesVisibility(testing = true) }) {
                        Text("Testing")
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().weight(0.8f)
                ) {
                    if (isStudentTableVisible) {
                        StudentsTableScreen()
                    } else if (isVariantsTableVisible) {
                        VariantsTableScreen()
                    } else if (isTestingTableVisible) {
                        TestingTableScreen()
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.1f)
                        .background(MaterialTheme.colors.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isStudentTableVisible) {
                        addInstrumentationButtons(
                            add = true,
                            find = true
                        )

                        if (isDialogOpen) {
                            if (dialogOperation == "add") {
                                openAddStudentDialog(onOpenDialog = { isDialogOpen = false })
                            } else if (dialogOperation == "find") {
                                openFindStudentDialog(onOpenDialog = { isDialogOpen = false })
                            }
                        }
                    } else if (isVariantsTableVisible) {
                        addInstrumentationButtons(
                            add = true,
                            find = true
                        )

                        if (isDialogOpen) {
                            if (dialogOperation == "add") {
                                openAddVariantDialog(onOpenDialog = { isDialogOpen = false })
                            } else if (dialogOperation == "find") {
                                openFindVariantDialog(onOpenDialog = { isDialogOpen = false })
                            }
                        }
                    } else if (isTestingTableVisible) {
                        addInstrumentationButtons(
                            add = false,
                            find = true
                        )

                        if (isDialogOpen) {
                            if (dialogOperation == "find") {
                                openFindTestingDialog(onOpenDialog = { isDialogOpen = false })
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun openAddStudentDialog(onOpenDialog: () -> Unit) {
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
                StudentsTable.refresh()
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
                modifier = Modifier.fillMaxSize(),
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
                            color = Color(0xFF33CC00),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "Person must have both first and last name!",
                            color = Color(0xFFFF0000),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun openFindStudentDialog(onOpenDialog: () -> Unit) {
        var personId by remember { mutableStateOf("") }
        var foundedPerson by mutableStateOf<StudentsTable.Student?>(null)

        var isOperationSuccessful by remember { mutableStateOf(false) }
        var isMessageVisible by remember { mutableStateOf(false) }

        val onFindClick:() -> Unit = {
            if (personId.isEmpty()) {
                isOperationSuccessful = false
            } else {
                foundedPerson = StudentsTable.studentsList.find { it.id == personId.toInt() }
                isOperationSuccessful = (foundedPerson != null)
            }

            isMessageVisible = true
            Timer().schedule(if (isOperationSuccessful) 5000 else 2000) {
                isMessageVisible = false
            }
        }

        Dialog(
            title = "Find person",
            onCloseRequest = onOpenDialog,
            state = rememberDialogState(position = WindowPosition(Alignment.Center))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = personId,
                    onValueChange = { personId = it },
                    label = { Text("Person ID") },
                    singleLine = true
                )
                Button(
                    onClick = onFindClick,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text("Find person")
                }

                AnimatedVisibility(
                    visible = isMessageVisible
                ) {
                    if (isOperationSuccessful) {
                        Text(
                            text = "${foundedPerson?.getStudentName()}",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "There is no person with $personId id!",
                            color = Color(0xFFFF0000),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun openAddVariantDialog(onOpenDialog: () -> Unit) {
        var newVariantName by remember { mutableStateOf("") }

        var isOperationSuccessful by remember { mutableStateOf(false) }
        var isMessageVisible by remember { mutableStateOf(false) }

        val onAddClick:() -> Unit = {
            if (newVariantName.isEmpty()) {
                isOperationSuccessful = false
            } else {
                if (newVariantName in VariantsTable.variantsList.map { it.name }) {
                    isOperationSuccessful = false
                } else {
                    VariantsTable.variantsList.add(
                        VariantsTable.Variant(
                            id = VariantsTable.currentId++,
                            name = newVariantName
                        )
                    )
                    isOperationSuccessful = true
                }
            }

            isMessageVisible = true
            Timer().schedule(if (isOperationSuccessful) 5000 else 2000) {
                isMessageVisible = false
            }
        }

        Dialog(
            title = "New variant",
            onCloseRequest = onOpenDialog,
            state = rememberDialogState(position = WindowPosition(Alignment.Center))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = newVariantName,
                    onValueChange = { newVariantName = it },
                    label = { Text("Variant name") },
                    singleLine = true
                )
                Button(
                    onClick = onAddClick,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text("Add new variant")
                }

                AnimatedVisibility(
                    visible = isMessageVisible,
                ) {
                    if (isOperationSuccessful) {
                        Text(
                            "New variant $newVariantName added!",
                            color = Color(0xFF33CC00),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "Variant $newVariantName already exits!",
                            color = Color(0xFFFF0000),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun openFindVariantDialog(onOpenDialog: () -> Unit) {
        var variantId by remember { mutableStateOf("") }
        var foundedVariant by mutableStateOf<VariantsTable.Variant?>(null)

        var isOperationSuccessful by remember { mutableStateOf(false) }
        var isMessageVisible by remember { mutableStateOf(false) }

        val onFindClick:() -> Unit = {
            if (variantId.isEmpty()) {
                isOperationSuccessful = false
            } else {
                foundedVariant = VariantsTable.variantsList.find { it.id == variantId.toInt() }
                isOperationSuccessful = (foundedVariant != null)
            }

            isMessageVisible = true
            Timer().schedule(if (isOperationSuccessful) 5000 else 2000) {
                isMessageVisible = false
            }
        }

        Dialog(
            title = "Find variant",
            onCloseRequest = onOpenDialog,
            state = rememberDialogState(position = WindowPosition(Alignment.Center))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = variantId,
                    onValueChange = { variantId = it },
                    label = { Text("Variant ID") },
                    singleLine = true
                )
                Button(
                    onClick = onFindClick,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text("Find variant")
                }

                AnimatedVisibility(
                    visible = isMessageVisible
                ) {
                    if (isOperationSuccessful) {
                        Text(
                            text = "${foundedVariant?.getVariantName()}",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "There is no variant with $variantId id!",
                            color = Color(0xFFFF0000),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun openFindTestingDialog(onOpenDialog: () -> Unit) {
        var fullName by remember { mutableStateOf("") }
        var foundedTestingPerson by mutableStateOf<TestingTable.Testing?>(null)

        var isOperationSuccessful by remember { mutableStateOf(false) }
        var isMessageVisible by remember { mutableStateOf(false) }

        val onFindClick:() -> Unit = {
            if (fullName.isEmpty()) {
                isOperationSuccessful = false
            } else {
                val tempFullName = fullName.split(" ").toMutableList()
                if (tempFullName.size == 2) {
                    tempFullName.add("-")
                }

                foundedTestingPerson = TestingTable.testingList.find {
                    it.studentFullName == tempFullName.joinToString(" ")
                }
                isOperationSuccessful = (foundedTestingPerson != null)
            }

            isMessageVisible = true
            Timer().schedule(if (isOperationSuccessful) 5000 else 2000) {
                isMessageVisible = false
            }
        }

        Dialog(
            title = "Find person's variant",
            onCloseRequest = onOpenDialog,
            state = rememberDialogState(position = WindowPosition(Alignment.Center))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Person full name") },
                    singleLine = true
                )
                Button(
                    onClick = onFindClick,
                    modifier = Modifier.width(280.dp)
                ) {
                    Text("Find variant")
                }

                AnimatedVisibility(
                    visible = isMessageVisible
                ) {
                    if (isOperationSuccessful) {
                        Text(
                            text = "${foundedTestingPerson?.variantName}",
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            "There is no $fullName student inside students table!",
                            color = Color(0xFFFF0000),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    fun launchMainScreen() = application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(
                placement = WindowPlacement.Floating,
                position = WindowPosition(Alignment.Center),
                size = DpSize(900.dp, 700.dp),
            )
        ) {
            App()
        }
    }
}