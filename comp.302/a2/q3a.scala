//Ian Benlolo 260744397
// q3a.scala
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
//as done in class
//the following are all the classes which basically just override
//the toString methods and print what is required
//i check if the input is a null list and  print nothing
case class ASToutertext(val in:String )extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "OUTERTEXT"
  }
}
case class ASTprogram(val out:List[ASTNode])extends ASTNode{
  override def toString:String = {
    if(out.length==0  ) ""
    else "PROGRAM" + "(" +out.mkString(" ") + ")"
  }
}
//following class actually parses the input string
//we call it on program which then automatically calls the correct methods
//for parsing/printing
class WMLparse extends WMLParser{
//a helper method
  def outertext:Parser[ASTNode] = Outertext ^^{
      case a:String => ASToutertext(a)
  }
  //this is the parser for only parsing basic outertext
  def program:Parser[ASTNode] = outertext ^^ {
    case a:ASTNode => { ASTprogram(List(a)) }
  }
}

object q3a {
  def main(args:Array[String]){
    val f : String = args(0)

    val fileLines = io.Source.fromFile(f).mkString
    val x = new WMLparse();

    val result = x.parseAll(x.program, fileLines)
    print(result)
  }
}
