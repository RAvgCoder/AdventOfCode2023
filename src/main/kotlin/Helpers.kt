import java.io.File

object MyArray {
    fun readFile(dayNum: Int, dayPart: Int = -1): List<String> {
        var currentDirectory =
            System.getProperty("user.dir") // Uses this to get the base dir eg "~/.../WordPuzzleSolver"

        if (!currentDirectory.endsWith("src")) currentDirectory += "/src"
        val dictionaryPath =
            "$currentDirectory/main/resources/${if (dayPart == -1) "Example" else "day${dayNum}.${dayPart}"}.txt"
        return mutableListOf<String>().also { File(dictionaryPath).forEachLine { line -> it.add(line) } }
    }

    fun print2D(arr: Array<Array<Any>>) {
        for (i in arr.indices) {
            for (j in arr[i].indices) {
                print(arr[i][j].toString() + " ")
            }
            println()
        }
    }
}
