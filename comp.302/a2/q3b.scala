//Ian Benlolo 260744397
// q3b.scala
//Assignment 2 comp 302 -Programming languages and Paradigms
// 28/02/18
import scala.util.parsing.combinator._

class WMLParser extends RegexParsers{
  val Tstart = "{{"
  val Tend = "}}"
  val Vstart = "{{{"
  val Vend = "}}}"
  val Dstart = "{''"
  val Dend = "'}"
  val Pipe = "|"
  val Pipes = "||"

//anything but {{ and {
  val Outertext = "^([^\\{]|(\\{(?!(\\{)|('))))+".r
  //anythign but {{, { ,{{{, |, ||, or }}
  val Inneritext = "^([^\\{\\|\\}]|(\\{(?!(\\{)|(\\{\\{)|(\\')))|\\}(?!(\\})))+".r
  //anything but {{, { ,{{{, |, ||, or '}
  val Innerdtext = "^([^\\{\\|\\']|(\\{(?!(\\{)|(\\{\\{)|(\\')))|\\'(?!(\\})))+".r
  //anything {{, { ,{{{ or '}
  val Bodytext = "^([^\\{\\|\\']|(\\{(?!(\\{)|(\\')|(\\{\\{)))|\\'(?!(\\})))+".r
  //anything but |, ||, }}}
  val Vname = "^([^\\|\\'\\}]|\\}(?!(\\}\\})))+".r

  //overriding whitespaces as the prof instructed in discussion board on mycourses
  override val whiteSpace = "".r
}


class ASTNode extends WMLParser
//classes to print
case class ASToutertext(val in:String )extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "OUTERTEXT"
  }
}
//program
case class ASTprogram(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) "PROGRAM"
    else "PROGRAM" + "(" +out.mkString(" ") + ")"
  }
}
//invoke
case class ASTinvoke(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) "INVOKE"
    else "INVOKE" + "(" +out.mkString(" ") + ")"
  }
}
//inneritext
case class ASTinneritext(val in:String) extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "INNERITEXT"
  }
}
//itext
case class ASTitext(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "ITEXT" + "(" +out.mkString(" ") + ")"
  }
}
//targs
case class ASTtargs(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "TARGS" + "(" +out.mkString(" ") + ")"
  }
}


//the parser
class WMLparse extends WMLParser{
//helper method to correctly parse the regex
  def inneritext:Parser[ASTNode] = Inneritext ^^{
    case y => ASTinneritext(y)
  }
  //helper method to correctly parse the regex
  def outertext:Parser[ASTNode] = Outertext^^{
    case a:String =>  ASToutertext(a)
  }

//program and the required methods for parsing basic outertext, and template invocations
  def program:Parser[ASTNode] = rep( outertext | invoke) ^^ {
    case a:List[ASTNode] =>  ASTprogram(a)
    case _ =>  ASTprogram( List() )
  }

  def invoke:Parser[ASTNode] = Tstart~ itext ~ targs ~ Tend ^^ {
    case Tstart ~ itext ~ targs ~ Tend =>{ ASTinvoke( List(itext, targs )) }
    case _ => ASTinvoke(List())
  }

  def itext:Parser[ASTNode] = rep ( inneritext | invoke )^^ {
    case Nil => {  ASTitext(List()) }
    case a => { ASTitext(a) }
  }

  def targs:Parser[ASTNode] = rep(Pipe ~ opt( itext )^^{
    case Pipe ~ None => ASTitext(List())
    case Pipe ~ Some(x) =>  x
    case _ => ASTitext(Nil)} )^^ {
      case list => ASTtargs(list)
  }
}
/main method for printing
object q3b {
  def main(args:Array[String]){
    val f : String = args(0)

    val fileLines = io.Source.fromFile(f).mkString
    val x = new WMLparse();

    val result = x.parseAll(x.program, fileLines)
    print(result)
  }
}
