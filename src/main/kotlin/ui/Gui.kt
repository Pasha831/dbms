package ui

import DbConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cached
import androidx.compose.material.icons.rounded.FindInPage
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
    private var isStartScreenVisible by mutableStateOf(true)
    private var isTablesScreenVisible by mutableStateOf(false)

    private var isStudentTableVisible by mutableStateOf(true)  // starting table
    private var isVariantsTableVisible by mutableStateOf(false)
    private var isTestingTableVisible by mutableStateOf(false)

    private var isDialogOpen by mutableStateOf(false)
    private var dialogOperation by mutableStateOf("")

    private var isOpenDBSuccessful by mutableStateOf(true)

    private var timeToUpdate by mutableStateOf(false)

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
        timeToUpdate.apply { /* nothing */ }

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
                            DbConstants.updateBackupFiles()
                            StudentsTable.deleteStudent(id)
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
        timeToUpdate.apply { /* nothing */ }

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
        timeToUpdate.apply { /* nothing */ }

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
            Spacer(Modifier.width(16.dp))
            FloatingActionButton(
                onClick = {
                    isDialogOpen = true
                    dialogOperation = "add"
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
            }
        }
        if (find) {
            Spacer(Modifier.width(16.dp))
            FloatingActionButton(
                onClick = {
                    isDialogOpen = true
                    dialogOperation = "find"
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(Icons.Rounded.FindInPage, contentDescription = null)
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

    private fun switchScreensVisibility(
        startScreen: Boolean = false,
        tablesScreen: Boolean = false
    ) {
        isStartScreenVisible = startScreen
        isTablesScreenVisible = tablesScreen
    }

    @Composable
    @Preview
    fun TablesScreen() {
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .weight(0.1f)
                        .fillMaxWidth()
                ) {
                    FloatingActionButton(
                        onClick = { switchScreensVisibility(startScreen = true) },
                        modifier = Modifier.padding(start = 8.dp).size(40.dp)
                    ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center
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
                        Spacer(Modifier.width(8.dp))
                    }
                    FloatingActionButton(
                        onClick = { inflateBackupDatabase() },
                        modifier = Modifier.padding(end = 8.dp).size(40.dp)
                    ) {
                        Icon(Icons.Rounded.Cached, contentDescription = null)
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
                DbConstants.updateBackupFiles()
                StudentsTable.addNewStudent(
                    newFirstname = newFirstname,
                    newLastname = newLastname,
                    newPatronymic = newPatronymic.ifEmpty { "-" }
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
                foundedPerson = StudentsTable.findStudent(personId.toInt())
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
                    DbConstants.updateBackupFiles()
                    VariantsTable.addNewVariant(newVariantName)
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
                foundedVariant = VariantsTable.findVariant(variantId.toInt())
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
                foundedTestingPerson = TestingTable.findTesting(fullName = fullName)
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

    private fun inflateNewDatabase() {
        DbConstants.createDatabaseFiles()

        VariantsTable.inflate(fromScratch = true)
        StudentsTable.inflate(fromScratch = true)
        TestingTable.inflate(fromScratch = true)

        switchScreensVisibility(tablesScreen = true)
    }

    private fun inflateExistingDatabase() {
        try {
            DbConstants.openExistingDirectory()

            VariantsTable.inflate(fromScratch = false)
            StudentsTable.inflate(fromScratch = false)
            TestingTable.inflate(fromScratch = false)

            switchTablesVisibility(students = true)
            switchScreensVisibility(tablesScreen = true)
        } catch (e: Exception) {
            isOpenDBSuccessful = false
            Timer().schedule(2000) {
                isOpenDBSuccessful = true
            }
        }
    }

    private fun inflateBackupDatabase() {
        DbConstants.loadBackupFiles()

        // stupid way to update live data in tables :)
        timeToUpdate = !timeToUpdate

        VariantsTable.inflate(fromScratch = false)
        StudentsTable.inflate(fromScratch = false)
        TestingTable.inflate(fromScratch = false)
    }

    @Composable
    private fun StartScreen() {
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome.",
                    style = MaterialTheme.typography.h2
                )
                Row {
                    Button(
                        onClick = {
                            inflateNewDatabase()
                        }
                    ) {
                        Text("Create database")
                    }
                    Spacer(Modifier.width(16.dp))
                    OutlinedButton(
                        onClick = {
                            inflateExistingDatabase()
                        }
                    ) {
                        Text("Open existing")
                    }
                }
                AnimatedVisibility(
                    visible = !isOpenDBSuccessful,
                ) {
                    Text(
                        "Something went wrong.",
                        color = Color(0xFFFF0000),
                        textAlign = TextAlign.Center
                    )
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
            if (isStartScreenVisible) {
                StartScreen()
            } else if (isTablesScreenVisible) {
                TablesScreen()
            }
        }
    }
}