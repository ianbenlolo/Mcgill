//Ian Benlolo 260744397
//fib2 calculates the n'th fibonacci number using tail recursion

def fib2(n: Int): Int = {
  def fib_tail(n: Int, a: Int, b: Int): Int = n match {
    case 0 => a
    case _ => { fib_tail(n-1, b, a + b) }
  }
  fib_tail(n,0,1)
  //tail recursion
  //similar to what we did in class

}
