import scala.util.parsing.combinator._
import scala.io.Source


//Ian Benlolo
//260744397
//comp 302 A3q.scala
//26/03/18

// -------------------------------------------------------------------------
// These classes form our AST

abstract class ASTNode(val kind : String)

// Outermost program structure
case class ASTProgram(val outer : List[ASTNode]) extends ASTNode("PROGRAM") {
  override def toString : String = kind+" ( " + outer.mkString(" ") + " )"
}

// The other non-terminals 
case class ASTInvoke(val name : ASTItext, val targs : ASTTargs) extends ASTNode("INVOKE") {
  override def toString : String = kind + " ( " + name  + " " + targs + " )"
}

case class ASTTargs(val targs : List[ASTItext]) extends ASTNode("TARGS") {
  override def toString : String = kind + " ( " + targs.mkString(" ") + " )"
}

case class ASTItext(val itext : List[ASTNode]) extends ASTNode("ITEXT") {
  override def toString : String = kind + " ( " + itext.mkString(" ") + " )"
}

case class ASTTvar(val name : String, val opt: ASTItext) extends ASTNode("TVAR") {
  override def toString : String = kind + " ( " + "VNAME" + { if (opt==null) "" else " " +opt.toString } + " )"
}

case class ASTTdef(val name : ASTDtext, val dparams : ASTDparams, val body : ASTDtext ) extends ASTNode("DEFINE") {
  override def toString : String = kind + " ( " + name  + " " + dparams + " " + body + " )"
}

case class ASTDparams(val dparams : List[ASTDtext]) extends ASTNode("DPARAMS") {
  override def toString : String = kind + " ( " + dparams.mkString(" ") + " )"
}

case class ASTDtext(val k:String, val dtext : List[ASTNode]) extends ASTNode(k) {
  override def toString : String = kind + " ( " + dtext.mkString(" ") + " )"
}

// Our various baseline forms of plain text, Outertext, Inneritext, Innerdtext, Bodytext
// These are really just tokens, but we use nodes to represent them in order to keep track of the contents.
case class ASTText(val k : String,val s : String) extends ASTNode(k) {
  override def toString : String = kind
}



// -------------------------------------------------------------------------
// Now the actual parser

class WMLParser extends RegexParsers {

  // Tokens.  We start with the fixed character sequences, just as strings.
  val TSTART = "{{"
  val TEND = "}}"
  val DSTART = "{'"
  val DEND = "'}"
  val VSTART = "{{{"
  val VEND = "}}}"
  val PIPE = "|"
  val PIPES = "||"
  
  // We also have tokens for more complex sequences, forming the outermost text,
  // as well as the inner text of invocations, definitions, and arguments.

  // anything but TSTART or DSTART
  val OUTERTEXT = "^([^{]|\\{(?!([{'])))+".r

  // anything but TSTART, DSTART, VSTART, PIPE(s), TEND
  val INNERITEXT = "^([^{|}]|\\{(?!([{|']))|\\}(?!\\}))+".r

  // anything but TSTART, DSTART, VSTART, PIPE(s), DEND
  val INNERDTEXT = "^([^{|']|\\{(?!([{|']))|'(?!\\}))+".r

  // anything but TSTART, DSTART, VSTART, DEND
  val BODYTEXT = "^([^{']|\\{(?!([{']))|'(?!\\}))+".r

  // anything but PIPE or VEND
  val VNAME = "^([^\\}|]|\\}(?!\\})|\\}\\}(?!\\}))+".r

  // Some helper functions

  // This deals with our optional component in Targs.
  // Here we assume a missing optional part is the empty string---that's actually
  // an over-assumption, but it is ok for now.
  def convertStringItextToTargs(x:List[String ~ Option[ASTItext]]) : ASTTargs = {
    new ASTTargs(x.map( (z) => z match {case s ~ Some(i) => i
      case s ~ None => new ASTItext(new ASTText("INNERITEXT","")::Nil) } ))
  }

  // A helper to convert dtext string, node pairs into just nodes.
  def convertStringItextToDparams(x:List[String ~ ASTDtext]) : ASTDparams = {
    new ASTDparams(x.map( {case s ~ i => i}))
  }

  // Now the grammar rules.

  // First a few trivial things.  We don't really need these as rules per se, as each of
  // these just converts a regular expression match into an ASTNode, but making separate
  // rules for these makes the type conversion easier.
  def textOuter: Parser[ASTNode] = OUTERTEXT ^^ { (x:String) => new ASTText("OUTERTEXT",x) }
  def textInner: Parser[ASTNode] = INNERITEXT ^^ { (x:String) => new ASTText("INNERITEXT",x) }
  def textInnerd: Parser[ASTNode] = INNERDTEXT ^^ { (x:String) => new ASTText("INNERDTEXT",x) }
  def textBody: Parser[ASTNode] = BODYTEXT ^^ { (x:String) => new ASTText("BODYTEXT",x) }

  // Our main entry point.
  // <program> ::= (OUTERTEXT |<invoke>|<define>)*
  def program: Parser[ASTNode] = rep( textOuter | invoke | define ) ^^ {
    (x:List[ASTNode]) => { new ASTProgram(x) }
  }

  // <invoke> ::= TSTART <itext> <targs> TEND
  def invoke: Parser[ASTInvoke] = TSTART ~ itext ~ targs ~ TEND ^^ {
    case _ ~ i ~ a ~ _ => { new ASTInvoke(i,a) }
  }
  // <targs> ::= (PIPE <itext>?)*
  def targs: Parser[ASTTargs] = rep( PIPE ~ opt(itext) ) ^^ { convertStringItextToTargs _ }

  // Here, note that we list tvar before invoke, to give it preference in parsing
  // <itext> ::= (INNERTEXT|<tvar>|<invoke>|<define>)*
  def itext: Parser[ASTItext] = rep( textInner | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTItext(x) } }

  // <tvar> ::= VSTART VNAME (PIPE itext)? VEND
  def tvar: Parser[ASTTvar] = VSTART ~ VNAME ~ opt(PIPE ~ itext) ~ VEND ^^ {
    case _ ~ n ~ None ~ _ => { new ASTTvar (n,null) }
    case _ ~ n ~ Some(_ ~ i) ~ _ => { new ASTTvar (n,i) }
  }

  // <define> ::= DSTART <dtextn> <dparams> PIPES <dtext> DEND
  def define: Parser[ASTTdef] = DSTART ~ dtextn ~ dparams ~ PIPES ~ dtextb ~ DEND ^^ {
    case _ ~ d ~ p ~ _ ~ b ~ _ => { new ASTTdef(d,p,b) }
  }
  // <dparams> ::= (SPIPE <dtextp>)* 
  def dparams: Parser[ASTDparams] = rep( PIPE ~ dtextp ) ^^ { convertStringItextToDparams _ }
  // We have 3 forms of dtext.  Each of them allows inner invokes, definitions, and args.
  // The template name can be empty, so we use * for repetition
  // <dtextn> ::= (INNERDTEXT|<tvar>|<invoke>|<define>)*
  def dtextn: Parser[ASTDtext] = rep( textInnerd | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTDtext("DTEXTN",x) } }
  // The parameters cannot be empty, so we use + (ie rep1) for repetition
  // <dtextp> ::= (INNERDTEXT|<tvar>|<invoke>|<define>)+
  def dtextp: Parser[ASTDtext] = rep1( textInnerd | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTDtext("DTEXTP",x) } }
  // Finally, the body itself can be empty, so we use * for repetition.  Different from a name, however,
  // the text itself can include pipe symbols.
  // <dtextb> ::= (BODYTEXT|<tvar>|<invoke>|<define>)*
  def dtextb: Parser[ASTDtext] = rep( textBody | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTDtext("DTEXTB",x) } }

  // We do not want whitespace discarded, we will do that ourselves
  override val whiteSpace = "".r
}



// And finally a program to invoke the parser on a file or input string
object WML extends App {
  def help(): Unit = {
    println("Specify (-s string|filename)")
    System.exit(1)
  }

  if (args.length==0) {
    help()
  }

  // taking a string input was not required, but it is convenient for testing, so allow it with a -s specifier.
  val source = args match {
    case Array(_,"-s",s) => s
    case Array("-s",s) => s
    case Array(_,fn) => Source.fromFile(fn, "UTF-8").mkString
    case Array(fn) => Source.fromFile(fn, "UTF-8").mkString
  }

  val p = new WMLParser
  val result = p.parseAll(p.program,source);
  if (result.successful) {
    println(result.get)
  } else {
    println("Parse failure: " + result);
  }
}
