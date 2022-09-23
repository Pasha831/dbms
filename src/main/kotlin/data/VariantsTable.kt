package data

import java.io.File

class VariantsTable {
    companion object {
        var currentId = 1
        val variantsList = mutableListOf<Variant>()
    }

    data class Variant(
        val id: Int,
        val name: String
    ) {
        override fun toString(): String {
            return "$id $name"
        }
        fun getVariantName(): String {
            return name
        }
    }

    fun inflate() {
        VariantsTable.variantsList.clear()

        // create directory with tables, if it doesn't exist
        File(DbConstants.tablesDirectory).mkdir()

        // output stream of information
        val outputStream = File(DbConstants.variantsTablePath).printWriter()

        for (id in 1..DbConstants.numberOfVariants) {
            val newVariant = VariantsTable.Variant(
                id = currentId,
                name = "variant${currentId++}"
            )

            VariantsTable.variantsList.add(newVariant)
        }

        // output each row of students table into the table
        outputStream.use { out ->
            VariantsTable.variantsList.forEach {
                out.println(it)
            }
        }
    }
}