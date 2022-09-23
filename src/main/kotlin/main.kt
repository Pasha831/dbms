import data.StudentsTable
import data.TestingTable
import data.VariantsTable
import ui.Gui

fun main() {
    val namesPath = "src\\main\\resources\\names.txt"

    val studentsTable = StudentsTable()
    val variantsTable = VariantsTable()
    val testingTable = TestingTable()

    studentsTable.inflate(namesPath)
    variantsTable.inflate()
    testingTable.inflate()

    val gui = Gui()
    gui.launchMainScreen()
}