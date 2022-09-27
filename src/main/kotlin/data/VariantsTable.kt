package data

import DbConstants
import java.io.File

class VariantsTable {
    companion object {
        var currentId = 1
        val variantsList = mutableListOf<Variant>()

        fun addNewVariant(name: String, isDefaultVariant: Boolean = false) {
            val newVariant = Variant(
                id = currentId++,
                name = name
            )
            if (!isDefaultVariant) {
                DbConstants.numberOfVariants++
            }
            variantsList.add(newVariant)
        }

        fun getVariant(studentId: Int): Variant {
            return variantsList[(studentId - 1) % DbConstants.numberOfVariants]
        }

        fun findVariant(variantId: Int): Variant? {
            return variantsList.find { it.id == variantId }
        }

        fun refresh() {
            // output stream of information
            val outputStream = File(DbConstants.variantsTablePath).printWriter()

            // output each row of students table into the table
            outputStream.use { out ->
                variantsList.forEach {
                    out.println(it)
                }
            }
        }
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
        // This naming only for based variants
        for (id in 1..DbConstants.numberOfVariants) {
            addNewVariant("variant$id", isDefaultVariant = true)
        }
        refresh()
    }
}