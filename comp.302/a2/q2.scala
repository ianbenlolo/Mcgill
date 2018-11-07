//Ian Benlolo 260744397
// q2.scala
//Assignment 2 comp 302 -Programming languages and Paradigms
// 28/02/18

import scala.util.parsing.combinator._
//as done in class
abstract class ASTNode extends WMLParser

//the following are all the classes which basically just override
//the toString methods and print what is required
//i check if the input is a null list and  print nothing
case class outertext(val in:String )extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "OUTERTEXT"
  }
}
case class inneritext(val in:String) extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "INNERITEXT"
  }
}
case class innerdtext(val in:String) extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "INNERDTEXT"
  }
}
case class bodytext(val in:String) extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "BODYTEXT"
  }
}
case class vname(val in:String) extends ASTNode{
  override def toString: String ={
    if(in.length() ==0) ""
    else "VNAME"
  }
}
case class program(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "PROGRAM" + "(" +out.mkString(" ") + ")"
  }
}
case class invoke(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "INVOKE" + "(" +out.mkString(" ") + ")"
  }
}
case class targs(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "TARGS" + "(" +out.mkString(" ") + ")"
  }
}
case class itext(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "itext" + "(" +out.mkString(" ") + ")"
  }
}
case class tvar(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "TVAR" + "(" +out.mkString(" ") + ")"
  }
}
case class define(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "DEFINE" + "(" +out.mkString(" ") + ")"
  }
}
case class dtextn(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "DTEXTN" + "(" +out.mkString(" ") + ")"
  }
}
case class dparams(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "DPARAMS" + "(" +out.mkString(" ") + ")"
  }
}
case class dtextp(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "DTEXTP" + "(" +out.mkString(" ") + ")"
  }
}
case class dtextb(val out:List[ASTNode] )extends ASTNode{
  override def toString:String = {
    if(out.length==0) ""
    else "DTEXTB" + "(" +out.mkString(" ") + ")"
  }
}
