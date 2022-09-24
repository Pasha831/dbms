package data

import DbConstants
import java.io.File

class TestingTable {
    companion object {
        val testingList = mutableListOf<Testing>()
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
        TestingTable.testingList.clear()

        // create directory with tables, if it doesn't exist
        File(DbConstants.tablesDirectory).mkdir()

        // output stream of information
        val outputStream = File(DbConstants.testingTablePath).printWriter()

        for (student in StudentsTable.studentsList) {
            val newVariantId = VariantsTable.variantsList[(student.id - 1) % DbConstants.numberOfVariants].id

            val newTesting = TestingTable.Testing(
                studentId = student.id,
                variantId = newVariantId,
                studentFullName = StudentsTable.studentsList.find { it.id == student.id }!!.getStudentName(),
                variantName = VariantsTable.variantsList.find { it.id == newVariantId }!!.getVariantName()
            )

            TestingTable.testingList.add(newTesting)
        }

        // output each row of students table into the table
        outputStream.use { out ->
            TestingTable.testingList.forEach {
                out.println(it)
            }
        }
    }
}