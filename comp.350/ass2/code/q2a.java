import java.math.*;

public class q2a{
  public static void main(String[] args){
    //Dynamic Programming method
    double[] x=new double[61];
    x[0]=1; //this is x_0
    for(int i=0; i<60; i++){
      x[i+1] = Math.pow(2,i+1)*(Math.sqrt(1+Math.pow(2,-i)*x[i])-1); 
    } //we just stored every value into an array using dynamic programming
    
    
    for(int i=1; i<61; i++){
      double y=x[i]-Math.log(2); // since x_0 is 1 its just ln(2)
      
      System.out.println(" For n= "+i+" : "+y);
      
    }
    
    double[] z=new double[61];       //q2b
    z[0]=1;
    for(int i=0; i<60; i++){
      z[i+1]=2*z[i]/(Math.sqrt(1+Math.pow(2,-i)*z[i])+1); 
      
    }
    System.out.println("q2b");
     for(int i=1; i<61; i++){
      double y=z[i]-Math.log(2);
      
      System.out.println(" For n= "+i+" : "+y);
      
    }
  }
}