import java.io.File
import java.text.SimpleDateFormat
import javax.swing.JFileChooser
import javax.swing.UIManager

/**
 * Useful constants of the database
 */
object DbConstants {
    const val namesPath = "tools\\names.txt"
    var numberOfVariants = 0
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
    }

    private fun createTablesDirectory() {
        File("./src/main/resources/tables").mkdirs()
    }

    fun openExistingDirectory() {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        createTablesDirectory()

        val fileChooser = JFileChooser("./src/main/resources/tables").apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            dialogTitle = "Select a folder"
            approveButtonText = "Select"
            approveButtonToolTipText = "Select directory"
        }
        fileChooser.showOpenDialog(null)

        // There could rise an exception, but I don't give a fuck about it.
        val selectedDatabase = fileChooser.selectedFile!!
        studentsTablePath = selectedDatabase.listFiles()!![0].path
        testingTablePath = selectedDatabase.listFiles()!![1].path
        variantsTablePath = selectedDatabase.listFiles()!![2].path
    }
}