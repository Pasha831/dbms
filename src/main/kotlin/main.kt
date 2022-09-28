import data.StudentsTable
import data.TestingTable
import data.VariantsTable
import ui.Gui

fun main() {
    val namesPath = "tools\\names.txt"

    VariantsTable.inflate()
    StudentsTable.inflate(namesPath)
    TestingTable.inflate()

    val gui = Gui()
    gui.launchMainScreen()
}