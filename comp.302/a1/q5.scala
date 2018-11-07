//Ian Benlolo 260744397
//shoelace formula calculates the area of any polygon of n>2 non-intersecting, connected straight line segments
// takes in a file name which stores n points which form the polygon

def shoelace(fileName:String): Double = {
//fileName is the name of a file containing x,y coords separated by spaces
    import scala.io.Source
    val points = Source.fromFile(fileName).getLines.toList
//sum1 stores the first summation of shoelace formula. reduce left adds the values calculated by the loop up
    val sum1 = (for (i<- 1 to (points.length-1)) yield (points(i-1).split(" ")(0).toDouble*points(i).split(" ")(1).toDouble)).reduceLeft((x,y)=> {x+y})
//sum2 stores the second summation

    val XnY1 =  points(0).split(" ")(1).toDouble* points(points.length-1).split(" ")(0).toDouble
    // the second summation of shoelace formula. redulceLeft just adds the values up

    val sum2 = (for(i<-1 to (points.length-1)) yield (points(i).split(" ")(0).toDouble * points(i-1).split(" ")(1).toDouble)).reduceLeft((x,y)=> {x+y})

    val X1Yn = points(0).split(" ")(0).toDouble * points(points.length-1).split(" ")(1).toDouble
//as per the formula, we take half the absolute val
    Math.abs(sum1 - sum2 + XnY1 - X1Yn)/2
}
