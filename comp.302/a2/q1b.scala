//Ian Benlolo 260744397
// q1b.scala
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
}
