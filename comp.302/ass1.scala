object assignment{
  def euler(k:Int): Double = {
    var sum:Double=1.0
    for(i<-2 to k) yield sum+=1.0/(i*i)
    math.sqrt(6.0*sum)
  }

  def fib2(n: Int): Int = {
    def fib_tail(n: Int, a: Int, b: Int): Int = n match {
      case 0 => a
      case _ => { fib_tail(n-1, b, a + b) }
    }
    fib_tail(n,0,1)
  }
  def show( input: List[Any]) : String = {
	def method(someList: List[Any], wtv2: String){
		if(someList.isEmpty){
		wtv2 = wtv2 + "Nil"
		}
	else{
		wtv2 = wtv2+ someList.head + "::("
		method(someList.tail, wtv2)
		}
		wtv2 = wtv2 +  ")"
	}
	var wtv:String = ""
	method(input, wtv)
}

def shoelace(filename:String): Double = {

import scala.io.Source

var lines = Source.fromFile(filename).getLines.toList

var sum1:Double=0.0
var sum2:Double=0.0
for(i<-1 to (lines.length-1)) yield sum1 += lines(i-1).split(" ")(0).toDouble * lines(i).split(" ")(1).toDouble
for(i<-1 to (lines.length-1)) yield sum2 += lines(i).split(" ")(0).toDouble * lines(i-1).split(" ")(1).toDouble
var insideAbs = lines(lines.length-1).split(" ")(0).toDouble * lines(0).split(" ")(1).toDouble - lines(0).split(" ")(0).toDouble*lines(lines.length-1).split(" ")(1).toDouble + sum1 - sum2
Math.abs(insideAbs)/2.0
}

/*def cakes(n:Int):Boolean = {
var firstCase:Int= (n/2)*2
var secondCase:Int=((n/3)*3)
var thirdCase:Int=((n/4)*4)
var fourthCase:Int=((n/5)*5)
  n match{
    case 40 => true
    case firstCase => cakes(n/2)
    case secondCase || thirdCase => {
      var first:Int = n%10
      var second:Int = (n%100)/10
      cakes(n-first-second)
    }
    case fourthCase=> cakes(n-40)
    case_ => false

  }
  }*/
  def cakes(n:Int):Boolean =>{
    val div = (n:Int, integrand:Int)=> {if((n/integrand)*integrand == n) true else false}
    for(integrand<-2 to 5) if div(n , integrand) integrand match{
      case 2 => cakes(n/2)
      case 3|4 => cakes(n-n%10-(n%100)/10)
      case 5=> cakes(n-40)
      case _=> false
    }
  false
}


}


i
}
