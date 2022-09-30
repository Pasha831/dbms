package data

import DbConstants
import java.io.File
import java.io.FileInputStream

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
        private var currentId = 1

        private fun refresh() {
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

        private fun addExistingStudent(
            id: Int,
            firstname: String,
            lastname: String,
            patronymic: String
        ) {
            val student = Student(
                id = id,
                firstname = firstname,
                lastname = lastname,
                patronymic = patronymic
            )
            currentId = id + 1

            studentsList.add(student)
        }

        fun deleteStudent(id: Int) {
            studentsList.removeIf { it.id == id }
            refresh()

            TestingTable.deleteTesting(id)
        }

        fun findStudent(studentId: Int): Student? {
            return studentsList.find { it.id == studentId }
        }

        fun inflate(fromScratch: Boolean = true) {
            studentsList.clear()
            currentId = 1

            // One difference: different sources of inputStream
            lateinit var inputStream: FileInputStream

            if (fromScratch) {
                inputStream = File(DbConstants.namesPath).inputStream()

                inputStream.bufferedReader().forEachLine {
                    val splitedLine = it.split(" ")

                    addNewStudent(
                        newFirstname = splitedLine[0],
                        newLastname = splitedLine[1],
                        newPatronymic = splitedLine[2]
                    )
                }

                refresh()
            } else {
                inputStream = File(DbConstants.studentsTablePath).inputStream()

                inputStream.bufferedReader().forEachLine {
                    val splitedLine = it.split(" ")

                    addExistingStudent(
                        id = splitedLine[0].toInt(),
                        firstname = splitedLine[1],
                        lastname = splitedLine[2],
                        patronymic = splitedLine[3]
                    )
                }
            }
        }
    }
}