import java.io.File
import java.nio.file.FileAlreadyExistsException
import java.time.Year
import kotlin.time.measureTimedValue

object MyArray {

    fun runPart(dayFuncToRun: (List<StringBuilder>) -> Unit, partNum: Int, dayNum: Int = 0) {
        println("//------------[Day $dayNum Part $partNum]------------\\\\")
        val readFile = readFile(dayNum)
        val (_, time1) = measureTimedValue {
            dayFuncToRun(readFile)
        }
        println("Time: ${time1.inWholeMilliseconds}ms\n")
    }

    /**
     * Reads a file and returns its content as a list of strings.
     *
     * @param dayNum the day number of the file (e.g., 1 for day1)
     * @return a list of strings containing the content of the file
     */
    private fun readFile(dayNum: Int = 0): List<StringBuilder> {


        // day1.txt
        val filePath =
            "${getFilePath()}/main/resources/${
                if (dayNum == 0)
                    "Example"
                else
                    "day${dayNum}"
            }.txt"

        return mutableListOf<StringBuilder>()
            .also {
                File(filePath)
                    .forEachLine { line ->
                        it.add(StringBuilder(line))
                    }
            }
    }

    private fun getFilePath(): String {
        // Uses this to get the base dir eg "~/.../WordPuzzleSolver"
        var currentDirectory = System.getProperty("user.dir")
        if (!currentDirectory.endsWith("src"))
            currentDirectory += "/src"
        return currentDirectory
    }

    /**
     * Prints a 2D array.
     *
     * @param arr the 2D array to be printed
     */
    fun print2D(arr: Array<Array<Any>>) {
        for (i in arr.indices) {
            for (j in arr[i].indices) {
                print(arr[i][j].toString() + "\t")
            }
            println()
        }
    }

    /**
     * Creates a new Kotlin file for a specific day.
     *
     * @param dayNum The number of the day for which the file is created.
     *               This should be a positive integer value.
     */
    fun newDay(dayNum: Int) {
        File("${getFilePath()}\\main\\kotlin\\Day$dayNum.kt")
            .also {
                if (it.exists())
                    throw FileAlreadyExistsException(
                        "Cannot create file as it already exits in ${it.absolutePath}"
                    )
            }
            .bufferedWriter()
            .use {
                it.write(
                    """
                    import MyArray.runPart
                    
                    /**
                     * https://adventofcode.com/${Year.now()}/day/$dayNum
                     */
                    fun main() {
                        // (partFunc, partNum, dayNum)
                        runPart(::part1, 1, $dayNum)
                        runPart(::part2, 2, $dayNum)
                    }
                    
                    private fun part1(readFile: List<StringBuilder>) {}
                    
                    private fun part2(readFile: List<StringBuilder>) {}
                    """.trimIndent()
                )
            }
    }
}
