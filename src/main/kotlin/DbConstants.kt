/**
 * Useful constants of the database
 */
object DbConstants {
    const val tablesDirectory = "src\\main\\resources\\tables"
    const val studentsTablePath = "$tablesDirectory\\students.txt"
    const val variantsTablePath = "$tablesDirectory\\variants.txt"
    const val testingTablePath = "$tablesDirectory\\testing.txt"
    private const val constNumberOfVariants = 5
    var numberOfVariants = constNumberOfVariants
}