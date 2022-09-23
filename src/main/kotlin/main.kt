import data.StudentsTable
import ui.Gui

fun main() {
    val namesPath = "src\\main\\resources\\names.txt"

    val studentsTable = StudentsTable()

    studentsTable.inflate(namesPath)
    studentsTable.show()

    println(studentsTable.findStudentById(-7))

    val gui = Gui()
    gui.launchScreen()
}