import java.io.File

object MyArray {

    /**
     * Reads a file and returns its content as a list of strings.
     *
     * @param dayNum the day number of the file (e.g., 1 for day1)
     * @param dayPart the part number of the file (e.g., 2 for part2). Defaults to -1 if not provided.
     * @return a list of strings containing the content of the file
     */
    fun readFile(dayNum: Int, dayPart: Int = -1): List<String> {
        // Uses this to get the base dir eg "~/.../WordPuzzleSolver"
        var currentDirectory = System.getProperty("user.dir")

        if (!currentDirectory.endsWith("src")) currentDirectory += "/src"

        // day1part2.txt
        val filePath =
            "$currentDirectory/main/resources/${
                if (dayPart == -1)
                    "Example"
                else
                    "day${dayNum}part${dayPart}"
            }.txt"

        return mutableListOf<String>().also { File(filePath).forEachLine { line -> it.add(line) } }
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
