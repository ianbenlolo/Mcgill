//Ian Benlolo 260744397
//approximates pi using eulers identiti of sum(1 to inf)of 1/n^2 = pi*pi / p
def euler(i:Int): Double = {
  Math.sqrt(6.*((for(k <- 1 to i) yield (1/(Math.pow(k,2)))).reduceLeft((x,y)=> {x+y})))
}
