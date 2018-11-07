//Ian Benlolo 260744397
//cakes 

def cakes(n:Int):Boolean = {
    if (n == 40) true  //base case
    //because impossible if n<40 ..
    else if (n<  40) false

    else{ // now n > 40
        val fCase = if (n%2==0) cakes(n/2) //if its divisible by 2, recursively call cakes with n/2 as input
        val sCase = if ((n%4==0 || n%3==0) && n%10!=0) cakes(n - ((n%100) * ((n%10) - (n%10))/10))
                  //(n%10) * ((n%100) - (n%10))/10) multiples together the last two digits so n- that returns the last two digits, as required
        val tCase = if (n%5 == 0) cakes(n-40) //as per the question

		(fCase == true || sCase == true|| tCase == true) //if one of them returned true, return true
		}
}
