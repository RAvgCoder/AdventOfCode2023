package HelperUtils

import java.io.File
import java.nio.file.FileAlreadyExistsException
import java.time.Year
import kotlin.time.measureTimedValue

object Utils {
    fun validate(message: String, givenLong: Long, expected: Long) {
        check(givenLong == expected) { "Your result=$givenLong but expected $expected" }
        println("$message $givenLong")
    }

    fun validate(message: String, givenInt: Int, expected: Int) {
        check(givenInt == expected) { "Your result is $givenInt but expected $expected" }
        println("$message $givenInt")
    }

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
    fun print2D(arr: Array<Array<Any>>) =
        StringBuilder().apply {
            arr.forEach { colm ->
                colm.forEach {
                    append("$it\t")
                }
                append("\n")
            }
            println(this)
        }


    fun print2D(arr: List<CharArray>) =
        StringBuilder().apply {
            arr.forEach { line ->
                line.forEach {
                    append("$it\t")
                }
                append("\n")
            }
            println(this)
        }


    /**
     * Creates a new Kotlin file for a specific day.
     *
     * @param dayNum The number of the day for which the file is created.
     *               This should be a positive integer value.
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
            .use {
                it.write(
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

    @JvmStatic
    fun main(args: Array<String>) {
        newDay(17)
    }
}
