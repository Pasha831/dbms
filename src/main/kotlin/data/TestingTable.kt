package data

import DbConstants
import java.io.File

class TestingTable {
    companion object {
        val testingList = mutableListOf<Testing>()

        fun refresh() {
            val outputStream = File(DbConstants.testingTablePath).printWriter()

            outputStream.use { out ->
                testingList.forEach {
                    out.println(it)
                }
            }
        }

        fun addNewTesting(
            student: StudentsTable.Student
        ) {
            val variant = VariantsTable.getVariant(student.id)
            val newTesting = Testing(
                studentId = student.id,
                variantId = variant.id,
                studentFullName = student.getStudentName(),
                variantName = variant.getVariantName()
            )

            testingList.add(newTesting)
            refresh()
        }

        fun findTesting(fullName: String): Testing? {
            val tempFullName = fullName.split(" ").toMutableList()
            if (tempFullName.size == 2) {
                tempFullName.add("-")
            }
            return testingList.find { it.studentFullName == tempFullName.joinToString(" ") }
        }

        fun deleteTesting(id: Int) {
            testingList.removeIf { it.studentId == id }
            refresh()
        }

        fun inflate(fromScratch: Boolean = true) {
            testingList.clear()

            if (fromScratch) {

            } else {

            }

            for (student in StudentsTable.studentsList) {
                addNewTesting(student = student)
            }

            refresh()
        }
    }

    data class Testing(
        val studentId: Int,
        val variantId: Int,
        val studentFullName: String,
        val variantName: String
    ) {
        override fun toString(): String {
            return "$studentFullName $variantName"
        }
    }
}