import java.util.*;

public class computeProduct{
  public static void main(String[] args){
   int count=args.length;
   ArrayList<String> evenArg=new ArrayList<String>(Arrays.asList(args));
   
   if(count%2!=0){ //we will iterate through args in 2's so to make sure we 
     evenArg.add(0,"1"); //have an even number of args and if we dont
     count++;        // add a "1" at the end which wouldnt change anything 
   }
   int k=0;  //the value of K
   float finl=1; //the final result well will be printing
   
   
   for (int i=0; i<count-1; i+=2){ //iterate through every inputted number
     float x= Float.parseFloat(evenArg.get(i)); float y=Float.parseFloat(evenArg.get(i+1));  
     float temp;//start of by parsing args to floats 
     
     while (doesOverflow(x,y)){ //if the multiplication would overflow
       k++;
       if(x >= Float.MIN_NORMAL*10) x/=10; //to account for underflowing of x
       else y/=10; //wouldnt underflow because its x*y is overflowing..
     }
     while(doesUnderflow(x,y)){
       k--;
       if(x <= Float.MAX_VALUE/10) x*=10;
       else y*=10;
     }
     temp=x*y; //this does not overflow or underflow 
     if(doesOverflow(temp, finl) || doesUnderflow(temp, finl)){ //we will do the same as before
       while(doesOverflow(temp, finl)){//just now we add x and y to 'finl'
         k++;
         if(temp>=Float.MIN_NORMAL*10) temp/=10;
         else finl/=10;
         }
       while (doesUnderflow(temp, finl)){
         k--;
         if(temp<=Float.MAX_VALUE/10) temp*=10;
         else finl*=10;
         }
       }
     finl*=temp; //wont over/under flow at this point!
     }
   System.out.println(""+finl+" times 10 to the power "+k);
  }
  public static boolean doesOverflow(float x, float y){ //simply checks if multiplying
    if (x*y >= Float.MAX_VALUE) return true; //these two numbers will overflow
    else return false;
  }
  public static boolean doesUnderflow(float x, float y){ //same but for underflowing
    if (x*y<=Float.MIN_NORMAL)return true;
    else return false;
  }
}