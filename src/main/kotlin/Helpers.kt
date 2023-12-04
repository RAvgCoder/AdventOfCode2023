import java.io.File
import kotlin.time.measureTimedValue

object MyArray {
    fun runPart(dayRun: (List<StringBuilder>) -> Unit, partNum: Int, dayNum: Int = -1) {
        println("//------------[Day $dayNum Part $partNum]------------\\\\")
        val readFile = readFile(dayNum)
        val (_, time1) = measureTimedValue {
            dayRun(readFile)
        }
        println("Time: ${time1.inWholeMilliseconds}ms\n")
    }

    /**
     * Reads a file and returns its content as a list of strings.
     *
     * @param dayNum the day number of the file (e.g., 1 for day1)
     * @return a list of strings containing the content of the file
     */
    private fun readFile(dayNum: Int = -1): List<StringBuilder> {
        // Uses this to get the base dir eg "~/.../WordPuzzleSolver"
        var currentDirectory = System.getProperty("user.dir")

        if (!currentDirectory.endsWith("src")) currentDirectory += "/src"

        // day1.txt
        val filePath =
            "$currentDirectory/main/resources/${
                if (dayNum == -1)
                    "Example"
                else
                    "day${dayNum}"
            }.txt"

        return mutableListOf<StringBuilder>()
            .also {
                File(filePath)
                    .forEachLine {
                        line -> it.add(StringBuilder(line))
                    }
            }
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
}
