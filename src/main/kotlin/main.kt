import data.StudentsTable

fun main() {
    val namesPath = "src\\main\\resources\\names.txt"

    val studentsTable = StudentsTable()

    studentsTable.inflate(namesPath)
    studentsTable.show()


}