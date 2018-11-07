//Ian Benlolo 260744397
// q3c.scala
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

case class ASToutertext(val in:String )extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "OUTERTEXT"
  }
}
case class ASTvname(val in:String)extends ASTNode{
  override def toString:String ={
    if (in.length() == 0) ""
    else "VNAME"
  }
}
case class ASTprogram(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) "PROGRAM"
    else "PROGRAM" + "(" +out.mkString(" ") + ")"
  }
}

  case class ASTinvoke(val out:List[ASTNode] )extends ASTNode{
    override def toString:String = {
      if(out.length==0) "INVOKE"
      else "INVOKE" + "(" +out.mkString(" ") + ")"
    }
  }
  case class ASTinneritext(val in:String) extends ASTNode{
    override def toString: String ={
      if(in.length() ==0) ""
      else "INNERITEXT"
    }
  }
  case class ASTitext(val out:List[ASTNode] )extends ASTNode{
    override def toString:String = {
      if(out.length==0) ""
      else "itext" + "(" +out.mkString(" ") + ")"
    }
  }
  case class ASTtargs(val out:List[ASTNode] )extends ASTNode{
    override def toString:String = {
      if(out.length==0) "TARGS"
      else "TARGS" + "(" +out.mkString(" ") + ")"
    }
  }

  case class ASTtvar (val out:List[ASTNode]) extends ASTNode {
    override def toString:String = {
      if(out.length== 0) ""
      else "TVAR" + "(" + out.mkString(" ") + ")"
    }
  }

class WMLParse extends WMLParser{
  def inneritext:Parser[ASTNode] = Inneritext ^^{
    case y => ASTinneritext(y)
  }
  def outertext:Parser[ASTNode] = Outertext^^{
    case a:String =>  ASToutertext(a)
  }
  def vname:Parser[ASTNode] = Vname^^ {
    case s:String => ASTvname(s)
  }

  def program:Parser[ASTNode] = rep( outertext | invoke ) ^^ {
    case a:List[ASTNode] => {  ASTprogram(a) }
    case _ => { ASTprogram( List() )}
  }

  def invoke:Parser[ASTNode] = Tstart~ itext ~ targs ~ Tend ^^ {
    case Tstart ~ itext ~ targs ~ Tend =>{ ASTinvoke( List(itext, targs )) }
    case _ => ASTinvoke(List())
  }

  def itext:Parser[ASTNode] = rep ( inneritext | tvar | invoke )^^ {
    case Nil => {  ASTitext(List()) }
    case a => { ASTitext(a) }
  }

  def targs:Parser[ASTNode] = rep(Pipe ~ opt( itext )^^{
    case Pipe ~ None => ASTitext(List())
    case Pipe ~ Some(x) =>  x
    case _ => ASTitext(Nil)
    })^^ {
      case list => { ASTtargs(list)}
  }

  def tvar:Parser[ASTNode] = Vstart ~ vname ~ opt (Pipe ~ itext) ~ Vend ^^ {
    case Vstart ~  Vname ~ Some(x) ~ Vend => ASTtvar (List(x._2))
    case Vstart ~ Vname ~ None ~ Vend => ASTtvar(List())
    case _ => ASTtvar (Nil)
    }
}

object q3c {
  def main(args:Array[String]){
    val f : String = args(0)

    val fileLines = io.Source.fromFile(f).mkString
    val x = new WMLparse();

    val result = x.parseAll(x.program, fileLines)
    print(result)
  }
}
