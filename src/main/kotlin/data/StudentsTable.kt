package data

import java.io.File

class StudentsTable {
    /**
     * Class that contains information about students
     * @param id id of the student
     * @param firstname first name of the student
     * @param lastname second name of the student
     * @param patronymic patronymic of the student
     */
    data class Student(
        val id: Int,
        val firstname: String,
        val lastname: String,
        val patronymic: String
    ) {
        override fun toString(): String {
            return "$id $firstname $lastname $patronymic"
        }
        fun getStudentName(): String {
            return "$firstname $lastname $patronymic"
        }
    }

    companion object {
        val studentsList = mutableListOf<Student>()
        var currentId = 1

        fun refresh() {
            val outputStream = File(DbConstants.studentsTablePath).printWriter()

            outputStream.use { out ->
                studentsList.forEach {
                    out.println(it)
                }
            }
        }

        /**
         * Add new student both to Students and Testing tables
         */
        fun addNewStudent(
            newFirstname: String,
            newLastname: String,
            newPatronymic: String
        ) {
            val newStudent = Student(
                id = currentId++,
                firstname = newFirstname,
                lastname = newLastname,
                patronymic = newPatronymic.ifEmpty { "-" }
            )
            studentsList.add(newStudent)
            refresh()

            TestingTable.addNewTesting(
                student = newStudent
            )
        }

        fun deleteStudent(id: Int) {
            studentsList.removeIf { it.id == id }
            refresh()

            TestingTable.deleteTesting(id)
        }

        fun findStudent(studentId: Int): Student? {
            return studentsList.find { it.id == studentId }
        }
    }

    /**
     * Inflates students table previously clearing studentsList.
     *
     * Creates directory with tables, reads input file and makes output table
     */
    fun inflate(filePath: String) {
        // create directory with tables, if it doesn't exist
        File(DbConstants.tablesDirectory).mkdir()

        // input and output streams of information
        val inputStream = File(filePath).inputStream()

        // read each line, split it and fill studentsList
        inputStream.bufferedReader().forEachLine {
            val splitedLine = it.split(" ")

            addNewStudent(
                newFirstname = splitedLine[0],
                newLastname = splitedLine[1],
                newPatronymic = splitedLine[2]
            )
        }

        refresh()
    }
}