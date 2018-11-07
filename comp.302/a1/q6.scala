//Ian Benlolo 260744397
//Takes in a String and returns a function that accepts a String and returns a string
//  does the entered operations on the string


def stringPipeline(letters: String): (String) => String = { (line: String ) => {
    //create a method that takes as input the index at in letters, and the string of words
    //uses composition to recursively edit the string in the correct order
    //returns the string x when we reach the end of the letters

    def pipeline(str: String, index: Int): String = {
        if (index == letters.length) str // index is the number of operaations
  // we have already done so if its == to numb of letter, we did all the operations
        else { //if not, we do following operation
            val ans : String = letters.charAt(index) match {
            case 'U' => str.toUpperCase //to upper case
            case 's' => str.sorted //sorts the chars
            case 'l' => str.toLowerCase //converts to lower case
            case 'T' => str.split(' ').map(_.capitalize).mkString(" ")
            case 'r' => str.reverse //reverse order
            case '*' => str.replaceAll("\\s", "") //deletes all space chars
            }
            pipeline(ans,index + 1) //do next operation
        }
    }
    pipeline(line, 0)
  }
}
