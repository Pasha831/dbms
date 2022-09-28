import java.io.File
import java.text.SimpleDateFormat

/**
 * Useful constants of the database
 */
object DbConstants {
    const val namesPath = "tools\\names.txt"
    private const val constNumberOfVariants = 5
    var numberOfVariants = constNumberOfVariants
    lateinit var currentDirectoryPath: String
    lateinit var studentsTablePath: String
    lateinit var variantsTablePath: String
    lateinit var testingTablePath: String

    fun createDatabaseFiles() {
        val currentTime = SimpleDateFormat("dd_M_yyyy_HH_mm_ss").format(System.currentTimeMillis())
        val newDirectoryName = "database_$currentTime"
        currentDirectoryPath = "./src/main/resources/tables/$newDirectoryName"

        studentsTablePath = "$currentDirectoryPath/students.txt"
        variantsTablePath = "$currentDirectoryPath/variants.txt"
        testingTablePath = "$currentDirectoryPath/testing.txt"

        File(currentDirectoryPath).mkdirs()
        File(studentsTablePath)
        File(variantsTablePath)
        File(testingTablePath)

        numberOfVariants = constNumberOfVariants
    }
}