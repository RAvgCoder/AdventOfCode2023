package HelperUtils

import java.io.File
import java.nio.file.FileAlreadyExistsException
import java.time.Year
import kotlin.time.measureTimedValue

object Utils {
    /**
     * Creates a file for a new day
     */
    @JvmStatic
    fun main(args: Array<String>) {
        newDay(21)
    }

    // ----------------------------- [Core_Utils] ----------------------------- \\
    /**
     * Executes a specific function associated with a day and part of a programming challenge.
     * This function is capable of handling different types of input data and measures the execution time of the associated function.
     *
     * @param T The type of input data.
     * @param dayFuncToRun A function that operates on a list of elements of type T.
     * @param partNum      An integer indicating the part of the challenge.
     * @param dayNum       (Optional) An integer representing the day of the challenge (defaults to 0 if not provided).
     * @throws IllegalArgumentException If an unsupported type is encountered.
     */
    inline fun <reified T> runPart(
        dayFuncToRun: (List<T>) -> Unit,
        partNum: Int,
        dayNum: Int = 0
    ) {
        println("//------------[Day $dayNum Part $partNum]------------\\\\")
        val readFile = when (T::class) {
            StringBuilder::class -> readFile(dayNum)
            CharArray::class -> readFile(dayNum).map { it.toString().toCharArray() }
            IntArray::class -> readFile(dayNum).map { line ->
                line.split("")
                    .drop(1)
                    .dropLast(1)
                    .map { it.toInt() }
                    .toIntArray()
            }

            else -> throw IllegalArgumentException("Unsupported type: ${T::class}")
        }
        val (_, elapsedTime) = measureTimedValue {
            dayFuncToRun(readFile as List<T>)
        }
        println("Time: ${elapsedTime.inWholeMilliseconds}ms\n")
    }

    fun validate(message: String, givenLong: Long, expected: Long) {
        check(givenLong == expected) { "Your result=$givenLong but expected $expected" }
        println("$message $givenLong")
    }

    fun validate(message: String, givenInt: Int, expected: Int) {
        check(givenInt == expected) { "Your result is $givenInt but expected $expected" }
        println("$message $givenInt")
    }

    // ----------------------------- [Core_Utils_END] ----------------------------- \\


    // ----------------------------- [Printers] ----------------------------- \\
    fun print2D(arr: Array<Array<Any>>) =
        StringBuilder().apply {
            arr.forEach { colm ->
                colm.forEach { elem ->
                    append("$elem\t")
                }
                append("\n")
            }
            println(this)
        }

    fun print2D(arr: List<CharArray>) =
        StringBuilder().apply {
            arr.forEach { line ->
                line.forEach { char ->
                    append("$char\t")
                }
                append("\n")
            }
            println(this)
        }

    // ----------------------------- [Printers_END] ----------------------------- \\


    // ----------------------------- [File_Utils] ----------------------------- \\
    /**
     * Retrieves the file path of the base directory for the project.
     *
     * @return A string representing the base directory path for the project.
     */
    private fun getFilePath(): String {
        // Uses this to get the base dir eg "~/.../WordPuzzleSolver"
        var currentDirectory = System.getProperty("user.dir")
        if (!currentDirectory.endsWith("src"))
            currentDirectory += "/src"
        return currentDirectory
    }


    /**
     * Reads a file and returns its content as a list of strings.
     *
     * @param dayNum the day number of the file (e.g., 1 for day1)
     * @return a list of strings containing the content of the file
     */
    fun readFile(dayNum: Int = 0): List<StringBuilder> {

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

    /**
     * Creates a new Kotlin file for a specific day of a programming challenge, populating it with a template structure.
     *
     * @param dayNum An integer representing the day for which the new Kotlin file is created.
     * @throws FileAlreadyExistsException If the file for the given day already exists.
     */
    private fun newDay(dayNum: Int) {
        val filPath = "${getFilePath()}\\main\\kotlin\\Day$dayNum.kt";
        File(filPath)
            .also {
                if (it.exists())
                    throw FileAlreadyExistsException(
                        "Cannot create file as it already exits in ${it.absolutePath}"
                    )
            }
            .bufferedWriter()
            .use { writer ->
                writer.write(
                    """
                    import HelperUtils.Utils.runPart
                    import HelperUtils.Utils.validate
                    
                    /**
                     * https://adventofcode.com/${Year.now()}/day/$dayNum
                     */
                    fun main() {
                        // (partFunc, partNum, dayNum)
                        runPart(::part1, 1, $dayNum)
                        runPart(::part2, 2, $dayNum)
                    }
                    
                    private fun part1(readFile: List<StringBuilder>) {
                        validate("", 0, 0)
                    }
                    
                    private fun part2(readFile: List<StringBuilder>) {
                        validate("", 0, 0)
                    }
                    """.trimIndent()
                )
            }
        println("File successfully created at location: $filPath")
    }

    /**
     * NOT A STABLE METHOD. ARGUMENT TYPE IS SUBJECT TO CHANGE. MEANT FOR TESTING PURPOSES
     */
    fun dumpArrayToFile(map: Array<CharArray>) {
        File("DUMP").bufferedWriter().use { writer ->
            map.forEach { arr ->
                writer.write(StringBuilder().let {
                    for (c in arr) {
                        it.append(c)
                    }
                    it.toString()
                })
                writer.write("\n")
            }
        }
    }    // ----------------------------- [File_Utils_END] ----------------------------- \\

}
