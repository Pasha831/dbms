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
    }

    /**
     * Inflates students table previously clearing studentsList.
     *
     * Creates directory with tables, reads input file and makes output table
     */
    fun inflate(filePath: String) {
        studentsList.clear()

        // create directory with tables, if it doesn't exist
        File(DbConstants.tablesDirectory).mkdir()

        // input and output streams of information
        val inputStream = File(filePath).inputStream()
        val outputStream = File(DbConstants.studentsTablePath).printWriter()

        // read each line, split it and fill studentsList
        inputStream.bufferedReader().forEachLine {
            val splitedLine = it.split(" ")

            val newStudent = Student(
                id = currentId++,
                firstname =  splitedLine[0],
                lastname =  splitedLine[1],
                patronymic =  splitedLine[2],
            )

            studentsList.add(newStudent)
        }

        // output each row of students table into the table
        outputStream.use { out ->
            studentsList.forEach {
                out.println(it)
            }
        }
    }

    /**
     * Shows students table in console
     */
    fun show() {
        println("Students Table:")
        studentsList.forEach { student ->
            println(student)
        }
    }

    /**
     * Finds a student inside a table. If there is no student with such id - returns warning.
     * @param id id of a student to find
     */
    fun findStudentById(id: Int): String {
        val student = studentsList.find { it.id == id }

        return if (student == null) {
            "No student with id $id in table!"
        } else {
            "${student.firstname} ${student.lastname} ${student.patronymic}"
        }
    }
}