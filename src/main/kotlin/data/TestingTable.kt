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

    fun inflate() {
        // create directory with tables, if it doesn't exist
        File(DbConstants.tablesDirectory).mkdir()

        for (student in StudentsTable.studentsList) {
            addNewTesting(student = student)
        }

        refresh()
    }
}