//Ian Benlolo 260744397
//Function show which takes in any List and prints it out in a recursive fashion

def show( input: List[Any]) : String = {
//def inner function to make it easier
//if input string is empty, return nil
    def innerFunction(inputList: List[Any], tmp: String) : String = {
        if(inputList.isEmpty) "Nil"
        else{ //else if we only have 1 elem, we return it and add ::Nil
            if(inputList.tail.isEmpty) tmp+inputList.head + "::Nil"
            else{ //recursively call until we only have one element
                innerFunction(inputList.tail, tmp+inputList.head+"::(" ) + ")"
            }
        }
    }
    innerFunction(input, "")
}
