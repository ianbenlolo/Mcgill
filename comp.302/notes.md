
#Tue 10 Apr 2018
---
Switch statement is a good example. What does that mean?
         We can translate this to nested if/then/else's // small step
         
Big step for a switch:

```         
<a1==a2, S> ↓ F (false) <switch a1 cases, S> ↓ S'
    ___________________________________________________
           
 <switch a, case a2 C cases, S> // C is a command
         
         
         
    <a1==a2, S> ↓ T (true) <C, S> ↓ S'
 _____________________________________________
   <switch a, case a2 C cases, S> ↓ S
         
         
 
 Default:
         
                    <C, S> ↓ S'
__________________________________________________
          <switch a, case a2 C cases, S> ↓ S'
```
               
   
Difficult example: 
         Semantics of WML are hard since since we have functions, scopes, and environments (incl linking, parents). Not that bad. Instead of S, we will have a parent env etc
         
Other language paradigms:
         
Declarative programming{
Functional is a kind of declarative programming.
Program specification is sufficient to get result. State what is true or should be true, and the system will figure this out
         
Pure functional is declarative, eg 
```x = foo(bar)```
is always true, since it's just like math. Impure means we have side effects like I/O, which could affect us.
         
Logic languages:
eg, Prolog. Not that popuar, but still interesting. Has performance issues.
In Prolog, we declare facts as simple things (rules/constraints), and relationships
Then, we query the system to get what we want.
         
Restricted set of logic rules, to allow inferences. These are called Horn clauses [A Horn 1951]
We can specify properties as disjunctions of literals, eg ```A v B v C v D```.
With Horn clauses, only one is positive, eg  ```~A v ~B v ~C v D``` means: "One of A or B or C is false, or D is true"
         
ie ```(A ^ B ^ C) => D``` "if all of A, B, and C are true, then D is true"
         
In Prolog we have this syntax:
```D :- A, B, C  % implied search (this is a comment)```
         
In order to show D, we must show A, B, and C // abuse notation.

eg:
 
```
         _______________
         | A      |         B
         |        |______
         |        |  C  |
         |________|_____|________
         |    E   |
         |        |        D 
         
         
         adj(A,B) :- true
         adj(A,C)
         adj(A,D)
         adj(A,D)
         
         adj(B,C)
         adj(B,D)
         adj(C,D)
         adj(D,E)
         
         % query
         ?- adj(B,C) % yes 
         
         ?- adj(B,A) % no. System does not know this from the rules
         ?- adj(E,C) % no
         
         % Now let's add this rule:
         adj(x,y) :- adj(y,x)
         
         % So now:
         ?- adj(B,A) % yes
         
         % x is a variable
         ?- adj(C,x) % also includes implicit case (x,C)
         % Prolog will answer with a list: x = A, x = B, x = C. Find all bindings for x for which query is true
         
         % This magic happens by doing an AI backtracking tree search.
         
         % Suppose we want to do graph coloring: Color each region st no two neiboring regions have same color
          _______________
         | A      |         B Blue
         |        |______
         | Red    |C (Grn)|
         |________|_______|________
         |    E   |
         |  Blue  |        D Yellow
         
         c(A, red)
         c(B, blue)
         c(C, green)
         c(D, yellow)
         c(E, blue)

         conflict :- adj(x,y), c(x, color), c(y, color)
         
         ?- conflict  % It's going to do lot of work (using adj etc) to say no
         
         % For this to work, we have to be talking 
         % about the same x, y, color on the RHS.Search is more complicated
         % Solution is to use Unification!
         
         % Unification of functions "very easy"
         % f(x1, x2, x3, ..., xn) =? g(y1, ..., ym)
         % Must have same arity (num of params) n == m, and f == g (eg ca't unify foo with bar)
         % Unify the arguments pairwise
         
         % assume a, b are atoms (basic things); x, y are variables.
         % a            a       unify   {}
         % f(3)         f(3)    unify   {}
         % a            b       No they do not unify. Maybe need to unify
         % f("hello")   f(3)    No
         % f(x)         f(2a)   Unify { x ⊢> 2a }
         % f(x)         f(y)    Unify { x ⊢> y } or { y ⊢> x }
         % f(x, a)      f(b, y) Unify {x ⊢> b} o {y ⊢> a}
         % f(x, a)      f(g(z),y) Unify {x ⊢> g(z)} o {y ⊢> a}
         
         % Still f(x,a) =? f(g(x),x) {x ⊢> g(x)} Not allowed! Infinite recursion
         % Why, because we want to replace x with g(x) to get x = g(x) = g(g(x)) = ...
         
         % f(x,x) =? f(a,b) where a != b. Can't Unify since we can't bind x twice!
         % f(x,x) =? f(y,a) Ok, as long as { y ⊢> a }
         
         
         } // end Declarative programming
```         
         
DYNAMIC TYPING
         
In Scala, it infers types, eg 
```
val x = 5
```
         
In dynamic typing, we figure out types at runtime, and we have automatic type-conversion

Some say these langs are "untyped" or "one type," but that's not stricly true. That is true for:         
Untyped lambda calculus, assembly language

Dynamic languages are scripting languages, like bash, Python, JavaScript.
         
In general, these are not used for large-scale programs, Python being the exception
         
Duck-typing: Don't care abt what type is. If it has the properties we want, Ok:

JavaScript example:

```javascript         
function Snake() {
	this.getSkin() = function () { // binding to a lambda abstraction
		return "smooth";
	}
}
         
function Potato() {
	this.getSkin() = function () {
		return "papery";
	}
}
         
(new Snake()).getSkin(); // smooth
(new Potato()).getSkin(); // papery
         
if(43<55){
	sp = new Snake(); // var, don't do this! Bound to global scope
} else {
	sp = new Potato();
}
sp.getSkin(); // smooth
         
         
} // dynamic typing
         
This does not work in Scala! Scala is a nominative 

    
class Snake{
	def getSkin() = { "smooth" }
}
    
class Potato{
	def getSkin() = { "papery" }
}
```    

Structural typing
    
	struct foo {
      int x;
      double d;
    }
    
    struct bar {
      int x;
      double d;
    }
    
Are these the same? No, but they  are interchangeable, and can be treated the same
    
We can do this in Scala, but it's not efficient
This will make Scala inspect itself 

```    
def foo(x: {def getSkin(): String }) = { 
// x is something that has a getSkin method
	x.getSkin();
}

foo(new Snake())
foo(new Potato()) // Both work!
```  
    
Dynamic types have disadvantages:
	- Hard to figure out somethings
    
eg in JavaScript: ```"5" + 3 is "53"```. What is ```"5" - 3```? ```String - String``` doesn't work, so it actually returns 2.

```  
if(false) "yes"; else "no" // no
if("") "yes"; else "no" // no 
if(0) "yes"; else "no" //no
if("0") "yes"; else "no" // yes
```

#Thu 12 Apr 2018
---
Issues in dynamic typing:
In javascript: 

- false &rarr; false 
- ø &rarr; false
	- non-ø &rarr; true
- "" (empty string)&rarr;false
 - non-empty string: true

 `true +"" -> "true"` but cant go back to bool.

--

__Esoteric languages__

Languages with really weird syntax. 

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; called "Turring complete" cuz it IS a complete language, but really weird.  

__Brain Fuck (1993)__
---> theres a lot of "fuck" languages.
 
Minimal language. 
we have a 1D array of all 0's. 

2 single_character commands

```
+,- inc/dec the value of the
<,>  move pointer to the left or right
. , output , input into the current

[ ] if the current cell is ø, skip command afthe 
	if non-ø, go back to the commant after the preceding left bracket
```

eg: a program to add 2 together 
` ,>,[<+>-]<.`<br/>
| n | m | &nbsp; | <br/>
after n iterations we should have <br/>
| n+m | ø | <br/>

---
__Befunge__
A language that was made to be "hard to compile". It's not really hard to compile, but its hard to understand. 

- 2D model of code -- its a 2 dimensional language
	- Code lives in an 80x25 wrap around grid(go off on top, come in on bottom, left goes to right,..).

- not a minimal language, has a lot of commands
- Start at top left, more left to right
- 28 commands
	- `[0-9]` &nbsp; push the #onto the stack
	- `+,-,*,/,%` &nbsp; `push(pop() op pop()) `
	- pop() or push
	- `<,>,^,v` &nbsp; changing direction of the code pointer 
	- `_`&nbsp; "move to the right `if pop() == 0` else go left.
	- `?` &nbsp; means go to a random direction 
	- p &nbsp; means pop y,x,v change the program at (x,y) to v

|&|>|:|`|-|:|v|.|v| |*|_|&|.|e|<br/>
| |^| | | | | _ |$| ....<br/>
somehow computes factorial

---
__Thue__ ~ 2000

&rarr; matrioshka language<br/>
&rarr; 2 pieces to it: 1. Set of rules defining replacement

` <  >::= <replacement>`<br/>
excution of this:
- find ___ of the LHS of a rule un the __ & replace it with the corresponding RHS
- continue until you cant do more replacements

idk had more things, not usefull

---
Last assignment: q2 we had to come up with a set of typing rules. 
we had

```
let x = &y in
	let z = &x in
		let w = *y in
			if (w == x) then
				let r = &&y in r
			else
				let r = **z in r
  // Solutions for A4:
       Q1. Everytime we see a lambda abs, do a anon func
       
Q2: Types are an inductive definition.
     tau ::= . | !tau
          
     let x=&y in
         let z=&x in
            let w=*z in
     
            if (w==x) then
                let r=*&y in r
            else
                let r=**z; in r
               
                  G(x): T
         Rvar___________________________ or Rvar___________________________
                G |-  x: T                           G, x: T |- x: T
                
                    G |- x:T    G |- y: T       // Disallow comparing a bool to a pointer
         Rcomp_______________________________
                    G |- x==y: bool
                
                G |- b: bool    G |- c: T     G |- d: T 
         Rif____________________________________________
                  G |- if(b) then c else d: T
                  
         Rlet same as before
          
                G |- x: !T                        // Take off one pointer level = dereference
         R*__________________________________     // ! is sufficient to say that only pointers allowed
                G |- *x : T
                
              G |- x: T
         R&_________________________
              G |- &x: !T
                  
                  
         Using ... is fine
         
         
         if statement does create new vars, so no need for left part of Rlet proof.
       


```

##GEt them

---
Exam info<br/>
like the quizs<br/>
short answer <br/>
covers the entire course <br/>
--> open book -> text, notes, wiki, ... no internet<br/>
-->  <br/>
--
Assignment 1 solutions

```
def euler(n:Int) : Double = {
  def eulerhelper(n:Int) : Double = {
    if (n==1) 1.0
    else eulerhelper(n-1)+1.0/(n*n)
  }
  Math.sqrt(eulerhelper(n)*6.0)
}
// --------------------------------------------------

//show List
def show(list:List[Any]) :String = {
  def showInner(list:List[Any]) :String = {
    if (list==Nil) "Nil"
    else "("+list.head+"::"+showInner(list.tail)+")"
  }
  if (list==Nil) "Nil"
  else list.head + "::"+showInner(list.tail)
}
// --------------------------------------------------

//Tail recursive fib
def fib2(n:Int) : Int = {

  def fibinner(n:Int) : (Int,Int) = {
    if (n<2) (1,1)
    else {
      val x = fibinner(n-1)
      (x._1 + x._2,x._1)
    }
  }
  fibinner(n)._1
}
// --------------------------------------------------

def cakes(n:Int) : Boolean = {
  if (n<40) false
  else if (n==40) true
  else {
    if (n%2==0 && cakes(n/2)) { return true }
    if ((n%3==0 || n%4==0) && n-(n%10)*((n%100)/10)!=n && cakes(n-(n%10)*((n%100)/10))) { return true }
    if (n%5==0 && cakes(n-40)) { return true }
    false
  }
}
// --------------------------------------------------

def shoelace(polygon:String) : Double = {
  val incoords = Source.fromFile(polygon).mkString.split("\\s+")
  val coords = for (i <- incoords.indices by 2) yield (incoords(i).toInt,incoords(i+1).toInt)

  def sum1(i:Int) : Int = {
    if (i==0) coords(0)._1*coords(1)._2
    else sum1(i-1) + coords(i)._1*coords(i+1)._2
  }

  def sum2(i:Int) : Int = {
    if (i==0) coords(1)._1*coords(0)._2
    else sum2(i-1) + coords(i+1)._1*coords(i)._2
  }
  val s1 = sum1(coords.length-2)
  val s2 = sum2(coords.length-2)
  0.5 * Math.abs(s1 + coords(coords.length-1)._1*coords(0)._2 - s2 -
      coords(0)._1*coords(coords.length-1)._2)
}
// --------------------------------------------------

/* String pipeline. where create val p = stringPipeline("Ts*"); p("abc def x ! 1") which would..
’U’ convert the string to upper-case ’l’ convert the string to lower-case
’T’ convert the string to title-case (capitalize the first non-space character,
    and the first character of each word, where words are sequences of characters
    separated by one or more spaces)
’r’ reverse the string
’s’ sort the characters
’*’ delete all space-characters
*/

def stringPipeline(s:String) : (String) => String = {
  def sp(s:String) : (String) => String = {
    if (s=="") (t:String) => t
    else {
      s(0) match {
      //.drop takes off first letter
      //"hello".drop(1) returns "ello"
        case 'U' => (t:String) => ((sp(s.drop(1))(t)).toUpperCase)
        case 'T' => (t:String) => {
          val v = (sp(s.drop(1))(t))
          val av = v.split("\\s")
            (for (a <- av) yield { if (a.length>0) a(0).toUpper+
                a.drop(1) else a }).mkString(" ")
        }
        case 'l' => (t:String) => ((sp(s.drop(1))(t)).toLowerCase)
        case 'r' => (t:String) => ((sp(s.drop(1))(t)).reverse)
        case 's' => (t:String) => ((sp(s.drop(1))(t)).sorted)
        case '*' => (t:String) => ((sp(s.drop(1))(t)).filter((c) => (c!=' ')))
        case _ => (t:String) => t
      }
    }
  }
  sp(s.reverse)
}

```
--

```
                      Some interesting scala quirks
val x = if(5<2) 1
  x: AnyVal = ()
val x = if(5<2) 1 else 2
  x: Int = 2
val x = if(5<2) 1 else "hello"
  x: Any = hello

for(i<-1 until 10)print(i)
  123456789
for(i<-1 to 10)print(i)
  12345678910
for(i<-1 until 11 by 2)print(i)
  13579


def tailfac(n: Int): Int m= {
   def tailHelper(n:Int, m:Int): Int m = {
     if(n==0) m else tailHelper(n-1,n*m)
   }
   tailHelper(n,1)
}

----> DECLARATIONS are unordered but ASSIGNMENTS are for vars/functions<-----

val y=8
def foo()={
  val x=y
  val y=2
}
error^! when assigning x, itll look in current env and find a y, but hasnt been
assigned yet. if didnt do val y=2, would have been fine cuz would have looked
in parent env.


Call-by-Name:
val x = 8
def ourConditional3(condition: Boolean, thenPart: => Unit, elsePart: => Unit) = {
      val x = 99
      if(condition) thenPart
      else elsePart
  }
ourConditional3(true, {println("Then side and x is" + x)},{println("Else side and x is" + x)})

 //prints Then side and x is 8<----- not 99! since the println function was defined
 //in global scope
```
--
```
		WML syntax
TSTART = "{{"
TEND = "}}"
VSTART = "{{{"
VEND = "}}}"
DSTART = "{'"
DEND = "'}"
PIPE = "|"
PIPES = "||"

OUTERTEXT = anything, except for TSTART, or DSTART
INNERITEXT = anything, except for TSTART, DSTART, VSTART, PIPE(s), TEND
INNERDTEXT = anything, except for TSTART, DSTART, VSTART, PIPE(s), DEND
BODYTEXT = anything, except TSTART, DSTART, VSTART, DEND
VNAME = anything, except for PIPE(s), VEND

<program> ::= (OUTERTEXT | <invoke> | <define>)*
<invoke> ::= TSTART <itext> <targs> TEND
<targs> ::= (PIPE <itext>?)*
<itext> ::= (INNERITEXT | <tvar> | <invoke> | <define> )* 

<tvar> ::= VSTART VNAME (PIPE <itext>)? VEND 

<define> ::= DSTART <dtextn> <dparams> PIPES <dtextb> DEND
<dtextn> ::= (INNERDTEXT | <invoke> | <define> | <tvar>)*
<dparams> ::= (PIPE <dtextp>)*
<dtextp> ::= (INNERDTEXT | <invoke> | <define> | <tvar>)+
<dtextb> ::= (BODYTEXT | <invoke> | <define> | <tvar>)*
```

