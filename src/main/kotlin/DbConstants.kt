import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import javax.swing.JFileChooser
import javax.swing.UIManager

/**
 * Useful constants of the database
 */
object DbConstants {
    const val namesPath = "./tools/names.txt"
    var numberOfVariants = 0
    lateinit var currentDirectoryPath: String
    lateinit var studentsTablePath: String
    lateinit var variantsTablePath: String
    lateinit var testingTablePath: String

    lateinit var backupFolderPath: String
    lateinit var backupStudentsTablePath: String
    lateinit var backupVariantsTablePath: String
    lateinit var backupTestingTablePath: String

    fun createDatabaseFiles() {
        val currentTime = SimpleDateFormat("dd_M_yyyy_HH_mm_ss").format(System.currentTimeMillis())
        val newDirectoryName = "database_$currentTime"
        currentDirectoryPath = "./src/main/resources/tables/$newDirectoryName"

        studentsTablePath = "$currentDirectoryPath/students.txt"
        variantsTablePath = "$currentDirectoryPath/variants.txt"
        testingTablePath = "$currentDirectoryPath/testing.txt"

        backupFolderPath = "$currentDirectoryPath/backup"
        backupStudentsTablePath = "$backupFolderPath/students.txt"
        backupVariantsTablePath = "$backupFolderPath/variants.txt"
        backupTestingTablePath = "$backupFolderPath/testing.txt"

        File(currentDirectoryPath).mkdirs()
        File(studentsTablePath)
        File(variantsTablePath)
        File(testingTablePath)

        File(backupFolderPath).mkdirs()
        File(backupStudentsTablePath).createNewFile()
        File(backupVariantsTablePath).createNewFile()
        File(backupTestingTablePath).createNewFile()
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
        studentsTablePath = selectedDatabase.listFiles()?.find { it.path.contains("students.txt")  }?.path ?: ""
        testingTablePath = selectedDatabase.listFiles()?.find { it.path.contains("testing.txt")  }?.path ?: ""
        variantsTablePath = selectedDatabase.listFiles()?.find { it.path.contains("variants.txt")  }?.path ?: ""

        currentDirectoryPath = selectedDatabase.path

        backupFolderPath = "$currentDirectoryPath/backup"
        backupStudentsTablePath = "$backupFolderPath/students.txt"
        backupVariantsTablePath = "$backupFolderPath/variants.txt"
        backupTestingTablePath = "$backupFolderPath/testing.txt"
    }

    fun updateBackupFiles() {
        Files.copy(
            File(studentsTablePath).toPath(),
            File(backupStudentsTablePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
        Files.copy(
            File(variantsTablePath).toPath(),
            File(backupVariantsTablePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
        Files.copy(
            File(testingTablePath).toPath(),
            File(backupTestingTablePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }

    fun loadBackupFiles() {
        Files.copy(
            File(backupStudentsTablePath).toPath(),
            File(studentsTablePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
        Files.copy(
            File(backupVariantsTablePath).toPath(),
            File(variantsTablePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
        Files.copy(
            File(backupTestingTablePath).toPath(),
            File(testingTablePath).toPath(),
            StandardCopyOption.REPLACE_EXISTING
        )
    }
}