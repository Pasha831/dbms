package data

import DbConstants
import java.io.File

class VariantsTable {
    companion object {
        private var currentId = 1
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
            refresh()
        }

        private fun addExistingVariant(id: Int, name: String) {
            val variant = Variant(
                id = id,
                name = name
            )
            DbConstants.numberOfVariants++
            variantsList.add(variant)
        }

        fun getVariant(studentId: Int): Variant {
            return variantsList[(studentId - 1) % DbConstants.numberOfVariants]
        }

        fun findVariant(variantId: Int): Variant? {
            return variantsList.find { it.id == variantId }
        }

        private fun refresh() {
            val outputStream = File(DbConstants.variantsTablePath).printWriter()

            outputStream.use { out ->
                variantsList.forEach {
                    out.println(it)
                }
            }
        }

        fun inflate(fromScratch: Boolean = true) {
            variantsList.clear()
            currentId = 1

            if (fromScratch) {
                DbConstants.numberOfVariants = 5

                // This naming only for based variants
                for (id in 1..DbConstants.numberOfVariants) {
                    addNewVariant("variant$id", isDefaultVariant = true)
                }

                refresh()
            } else {
                DbConstants.numberOfVariants = 0
                val inputStream = File(DbConstants.variantsTablePath).inputStream()

                inputStream.bufferedReader().forEachLine {
                    val splitedLine = it.split(" ")

                    addExistingVariant(
                        id = splitedLine[0].toInt(),
                        name = splitedLine.subList(1, splitedLine.size).joinToString(" ")
                    )

                    currentId = splitedLine[0].toInt()
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
}