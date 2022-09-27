import data.StudentsTable
import data.TestingTable
import data.VariantsTable
import ui.Gui

fun main() {
    val namesPath = "src\\main\\resources\\names.txt"

    val studentsTable = StudentsTable()
    val variantsTable = VariantsTable()
    val testingTable = TestingTable()

    variantsTable.inflate()
    studentsTable.inflate(namesPath)
    testingTable.inflate()

    val gui = Gui()
    gui.launchMainScreen()
}