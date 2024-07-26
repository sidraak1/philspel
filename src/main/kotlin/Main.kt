package edu.davis.cs.ecs36c.homework0

import java.io.File
import java.io.FileNotFoundException


val defaultFile = "/usr/share/dict/words"

// A regular expression pattern that will match words:

// A useful trick: Given the regular expression object re,
// re.findAll returns a Collection (an iteratable structure)
// that contains the words, while re.split() returns an array
// of the strings that AREN'T matched.  So you can take an input
// line, create both the split and the collection, and iterate over
// the collection keeping track of the iteration count.
val splitString = "\\p{Alpha}+"

/**
 * This function should take a filename (either the default file or the
 * one specified on the command line.)  It should create a new MutableSet,
 * open the file, and load each line into the set.
 *
 * @param filename may not exist, and in that case the function should
 * throw a FileNotFound exception.
 */
fun loadFile(filename: String): Set<String>{
    val set = mutableSetOf<String>()

    // Create a File object
    val file = File(filename)

    // If file doesn't exist, throw a FileNotFoundException
    if (!file.exists()) throw FileNotFoundException("File not found: $filename")

    // Iterate through each line of the file using forEachLine
    file.forEachLine { line -> set.add(line) }

    return set
}

/**
 * This function should check if a word is valid by checking the word,
 * the word in all lower case, and the word with all but the first character
 * converted in lower case with the first character unchanged.
 */
fun checkWord(word: String, dict: Set<String>) : Boolean{

    // Check if the word is in the dictionary
    val isWordValid = dict.contains(word)

    // Check if the word in all lowercase is in the dictionary
    val isLowerCaseValid = dict.contains(word.lowercase())

    // Check if the word with all letters except the first one is lowercase is in the dictionary
    val isFirstCharUpperCaseValid = dict.contains(word.first().toString() + word.drop(1).lowercase())

    // Return true if at least one of these three is present in the dictionary
    return isWordValid || isLowerCaseValid || isFirstCharUpperCaseValid
}

/**
 * This function should take a set (returned from loadFile) and then
 * processes standard input one line at a time using readLine() until standard
 * input is closed
 *
 * Note: readLine() returns a String?: that is, a string or null, with null
 * when standard input is closed.  Under Unix or Windows in IntelliJ you can
 * close standard input in the console with Control-D, while on the mac it is
 * Command-D
 *
 * Once you have the line you should split it with a regular expression
 * into words and nonwords,
 */
fun processInput(dict: Set<String>){
    val re = Regex(splitString)

    // Input processed one line at a time until closed
    while (true) {

        // Exit loop if standard input is closed
        val line = readLine() ?: break

        // Finds all words and put into a list
        val words = re.findAll(line).toList()

        // If the list does not have words, print original input and continue out of the loop
        if (words.isEmpty()) {
            print(line)
            println()
            continue
        }

        // Process each word or non-word in the line
        for ((index, match) in words.withIndex()) {

            // The actual value is stored in match.value
            val item = match.value

            // Preserve original case of word from match
            val originalItem = line.substring(match.range).trim()

            // Checks if the word matches the Regex pattern and if the word is in the dictionary
            if (item.matches(re) && checkWord(item, dict)) {
                // If the word is in the dictionary, print it
                print(originalItem)
            } else {
                // If the item is non-word or not in dictionary, print [sic] next to it
                print("$originalItem [sic]")
            }

            // Add spaces and period after each word
            if (index < words.size - 1) {
                // Add spaces between words based on original input
                val spaces = line.substring(match.range.last + 1, words[index + 1].range.first)
                print(spaces)
            // If there is something after the last word, print it
            } else {
                print(line.substring(words[index].range.last + 1, line.length))
            }
        }
            // Print new line at the end of each processed line
            println()
    }
}

/**
 * Your main function should accept an argument on the command line or
 * use the default filename if no argument is specified.  If the dictionary
 * fails to load with a FileNotFoundException it should use
 * kotlin.system.exitProcess with status code of 55
 */
fun main(args :Array<String>) {
    // If command line arg not available, use default file
    val filename = args.getOrNull(0) ?: defaultFile

    // Create a File object
    val file = File(filename)

    // Check if file exists
    if (file.exists()) {
        // Load dictionary from file
        val dictionary = loadFile(filename)

        // Process standard input one line at a time
        processInput(dictionary)
    }
    // If file doesn't exist
    else {
        // Print error message and exit using exit code 55
        println("Error: File not found: $filename")
        kotlin.system.exitProcess(55)
    }
}