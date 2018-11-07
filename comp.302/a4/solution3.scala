import scala.util.parsing.combinator._
import scala.io.Source
import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox

// -------------------------------------------------------------------------
// These classes form our AST

abstract class ASTNode(val kind : String)

// Outermost program structure
class ASTProgram(val outer : List[ASTNode]) extends ASTNode("program") {
  override def toString : String = outer.mkString(",")
}

// Our various forms of plain text
class ASTText(val s : String,val k : String) extends ASTNode(k) {
  override def toString : String = kind + "(" + s + ")"
}
class ASTTextOuter(val text : String) extends ASTText(text,"TEXT")
class ASTTextInner(val text : String) extends ASTText(text,"ITEXT")
class ASTTextInnerd(val text : String) extends ASTText(text,"DTEXT")
class ASTTextBody(val text : String) extends ASTText(text,"BTEXT")

// The other non-terminals 

class ASTInvoke(val name : ASTItext, val targs : ASTTargs) extends ASTNode("invoke") {
  override def toString : String = kind + "(" + name  + "),args(" + targs + "))"
}

class ASTTargs(val targs : List[ASTItext]) extends ASTNode("targs") {
  override def toString : String = if (targs.length==0) "" else targs.mkString(",")
}

class ASTItext(val itext : List[ASTNode]) extends ASTNode("itext") {
  override def toString : String = itext.mkString(",")
}

class ASTTvar(val name : String, val opt: ASTItext) extends ASTNode("tvar") {
  override def toString : String = kind + "(" + name + { if (opt==null) "" else "|"+opt } + ")"
}

class ASTTdef(val name : ASTDtext, val dparams : ASTDparams, val body : ASTDtext ) extends ASTNode("tdef") {
  override def toString : String = kind + "(" + name  + ",params(" + dparams + "),body(" + body + "))"
}

class ASTDparams(val dparams : List[ASTDtext]) extends ASTNode("dparams") {
  override def toString : String = if (dparams.length==0) "" else dparams.mkString(",")
}

class ASTDtext(val dtext : List[ASTNode]) extends ASTNode("dtext") {
  override def toString : String = dtext.mkString("")
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

  // PIPE is a bit trickier, as it cannot be followed by another pipe
  //val PIPE = "^\\|(?!\\|)".r
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

  def convertStringItextToTargs(x:List[String ~ Option[ASTItext]]) : ASTTargs = {
    new ASTTargs(x.map( (z) => z match {case s ~ Some(i) => i
      case s ~ None => new ASTItext(new ASTTextInner("")::Nil) } ))
  }

  def convertStringItextToDparams(x:List[String ~ ASTDtext]) : ASTDparams = {
    new ASTDparams(x.map( {case s ~ i => i}))
  }

  // Now the grammar rules.

  // First a few trivial things.  We don't really need these as rules per se, as each of
  // these just converts a regular expression match into an ASTNode, but making separate
  // rules for these makes the type conversion easier.

  def textOuter: Parser[ASTNode] = OUTERTEXT ^^ { (x:String) => new ASTTextOuter(x) }
  def textInner: Parser[ASTNode] = INNERITEXT ^^ { (x:String) => new ASTTextInner(x) }
  def textInnerd: Parser[ASTNode] = INNERDTEXT ^^ { (x:String) => new ASTTextInnerd(x) }
  def textBody: Parser[ASTNode] = BODYTEXT ^^ { (x:String) => new ASTTextBody(x) }
  
  // <program> ::= (OUTERTEXT |<invoke>|<define>)*
  def program: Parser[ASTNode] = rep( textOuter | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTProgram(x) } }

  // <invoke> ::= TSTART <itext> <targs> TEND
  def invoke: Parser[ASTInvoke] = TSTART ~ itext ~ targs ~ TEND ^^ { case _ ~ i ~ a ~ _ => { new ASTInvoke(i,a) } }
  // <targs> ::= (PIPE <itext>)*
  def targs: Parser[ASTTargs] = rep( PIPE ~ opt(itext) ) ^^ { convertStringItextToTargs _ }
  // note that we list tvar before invoke, to give it preference in parsing
  // <itext> ::= (INNERTEXT |<invoke>|<define>|<tvar>)*
  def itext: Parser[ASTItext] = rep( textInner | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTItext(x) } }

  // <tvar> ::= VSTART VNAME (PIPE itext)? VEND
  def tvar: Parser[ASTTvar] = VSTART ~ VNAME ~ opt(PIPE ~ itext) ~ VEND ^^
                              { case _ ~ n ~ None ~ _ => { new ASTTvar (n,null) }
                                case _ ~ n ~ Some(_ ~ i) ~ _ => { new ASTTvar (n,i) }
                              }

  // <define> ::= DSTART <dtextn> <dparams> PIPES <dtext> DEND
  def define: Parser[ASTTdef] = DSTART ~ dtextn ~ dparams ~ PIPES ~ dtextb ~ DEND ^^ { case _ ~ d ~ p ~ _ ~ b ~ _ => { new ASTTdef(d,p,b) } }
  // <dparams> ::= (SPIPE <dtextp>)* 
  def dparams: Parser[ASTDparams] = rep( PIPE ~ dtextp ) ^^ { convertStringItextToDparams _ }
  // note that we list tvar in the dtext forms before invoke, to give it preference in parsing
  // We have 3 forms of dtext.  Each of them allows inner invokes, definitions, and args.
  // The template name can be empty, so we use * for repetition
  // <dtextn> ::= (INNERDTEXT |<templateinvocation>|<templatedef>|<tvar>)*
  def dtextn: Parser[ASTDtext] = rep( textInnerd | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTDtext(x) } }
  // The parameters cannot be empty, so we use + for repetition
  // <dtextp> ::= (INNERDTEXT |<templateinvocation>|<templatedef>|<tvar>)+
  def dtextp: Parser[ASTDtext] = rep1( textInnerd | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTDtext(x) } }
  // Finally, the body itself can be empty, so we use * for repetition.  Different from a name, however,
  // the text itself can include pipe symbols.
  // <dtextb> ::= (BODYTEXT |<templateinvocation>|<templatedef>|<tvar>)*
  def dtextb: Parser[ASTDtext] = rep( textBody | tvar | invoke | define ) ^^ { (x:List[ASTNode]) => { new ASTDtext(x) } }

  // We do not want whitespace discarded, we will do that ourselves
  override val whiteSpace = "".r
}

// -------------------------------------------------------------------------
// Bindings and environments

// Symbols are bound to either strings or function definitions.
// The abstract class encapsulates both.
abstract class Bound
case class BoundToText(text : String) extends Bound
// A function binding is more complex.  We record the (evaluated) list of paramaters,
// the unevaluated body, and the defining environment.
case class BoundToFunc(params : List[String], body : ASTDtext, env : Environment) extends Bound

// An environment is a set of bindings, and a parent environment
class Environment(val parent: Environment) {

  private val bindings = scala.collection.mutable.HashMap[String, Bound]()

  def addBinding(k : String, v : Bound) : Unit = {
    bindings(k) = v;
  }
  def getBinding(k : String) : Bound = {
    bindings.getOrElse(k,null)
  }
  def getBindingRec(k : String) : Bound = {
    if (parent==null)
      getBinding(k)
    else
      bindings.getOrElse(k,parent.getBindingRec(k))
  }
}

// -------------------------------------------------------------------------
// Evaluating it

object Evaluator {
  // A basic pretty printer
  def pprint(root : ASTNode) : String = {
    val StartsWithBracket = "(\\{.*)".r
    root match {
      case n : ASTProgram => n.outer.map(pprint _).mkString("")
      case n : ASTText => n.s
      case n : ASTInvoke => "{{" + {
        val t = pprint(n.name).trim
        t match {
          case StartsWithBracket(_) => " "+t
          case _ => t
        }
      } + pprint(n.targs) + "}}"
      case n : ASTTargs => {
        val targs = n.targs.map(pprint _).mkString("|")
        if (targs=="") ""
        else "|" + targs
      }
      case n : ASTItext => n.itext.map(pprint _).mkString("")
      case n : ASTTvar => "{{{" + n.name.trim + { if (n.opt==null) "" else "|"+pprint(n.opt) } + "}}}"
      case n : ASTTdef => "{:" + pprint(n.name).trim + pprint(n.dparams) + "||" + pprint(n.body) + ":}"
      //case n : ASTDparams => "|" + ({ for (i <- 0 until n.dparams.length-1) yield pprint(n.dparams(i)).trim } :+ pprint(n.dparams.last)).mkString("|")
      case n : ASTDparams => {
        val dparams = n.dparams.map((x) => pprint(x).trim).mkString("|")
        if (dparams=="") ""
        else "|" + dparams
      }
      case n : ASTDtext => n.dtext.map(pprint _).mkString("")
      case _ => "***"
    }
  }

  // For consistent display of error messages from malformed eval results
  def errorText(s : String) : String = {
    "<span style=\"color:red;\">ERROR(" + s + ")</span>"
  }

  // An advanced evaluator.  This allows for anonymous functions,
  // as well as function passing and return capabilities.
  // The way we do this is by returning a pair---a string and a function (as a binding object).
  // Rules which expect a string use the string portion.  Rules which can handle a function
  // first verify that the string is empty (this to allow function objects to be surrounded by whitespace)
  // and if so then can use the function object, otherwise do whatever it is they do with a string.
  def evaladv(root : ASTNode, e : Environment) : (String,BoundToFunc) = {

    // Some handlers for special functions
    def specialIf(i : ASTInvoke, e : Environment) : (String,BoundToFunc) = {
      if (i.targs.targs.length<2) {
        (errorText("#if missing arguments"),null)
      } else {
        val cond = evaladv(i.targs.targs.head,e)._1.trim
        i.targs.targs match {
          case _::thenpart::elsepart::Nil => if (cond!="") evaladv(thenpart,e) else evaladv(elsepart,e)
          case _::thenpart::Nil => if (cond!="") evaladv(thenpart,e) else ("",null)
          case _ => (errorText("#if excess arguments"),null)
        }
      }
    }

    def specialIfeq(i : ASTInvoke, e : Environment) : (String,BoundToFunc) = {
      if (i.targs.targs.length<3) {
        (errorText("#ifeq missing arguments"),null)
      } else {
        val cond1 = evaladv(i.targs.targs.head,e)._1.trim
        val cond2 = evaladv(i.targs.targs.tail.head,e)._1.trim
        i.targs.targs match {
          case _::_::thenpart::elsepart::Nil => if (cond1==cond2) evaladv(thenpart,e) else evaladv(elsepart,e)
          case _::_::thenpart::Nil => if (cond1==cond2) evaladv(thenpart,e) else ("",null)
          case _ => (errorText("#ifeq excess arguments"),null)
        }
      }
    }

    def specialExpr(i : ASTInvoke, e : Environment) : (String,BoundToFunc) = {
      if (i.targs.targs.length<1) {
        (errorText("#expr missing arguments"),null)
      } else if (i.targs.targs.length>1) {
        (errorText("#expr too many arguments"),null)
      } else {
        val op = evaladv(i.targs.targs.head,e)._1.trim
        val tb = universe.runtimeMirror(getClass.getClassLoader).mkToolBox()
        //println("Expression on \""+op+"\"")
        (tb.eval(tb.parse(op)).toString,null)
      }
    }

    // Special templates, checked for during invocation
    val specialTemplates = Map("#if" -> specialIf _, "#ifeq" -> specialIfeq _, "#expr" -> specialExpr _)

    // Handling an invocation
    def newInvoke(i : ASTInvoke, e : Environment) : (String,BoundToFunc) = {
      val invokee = evaladv(i.name,e)
      val name = invokee._1.trim
      // First check for our special templates
      if (specialTemplates.contains(name)) {
        specialTemplates(name)(i,e)
      } else {
        // if we got a non-empty string, we look it up to find the binding, otherwise
        // we use the binding that was returned
        val template = if (name!="") e.getBindingRec(name) else invokee._2
        template match {
          case null => (errorText("no template found: " + name),null)
          case t : BoundToText => (errorText(name + " is not a template"),null)
          case t : BoundToFunc => {
            val targs = i.targs.targs.map((x) => evaladv(x,e))
            val e1 = new Environment(t.env)
            val bindings = t.params.zip(targs.map((x) => {if (x._1.trim=="" && x._2!=null) x._2 else new BoundToText(x._1)}))
            for (b <- bindings) {
              e1.addBinding(b._1,b._2)
            }
            evaladv(t.body,e1)
          }
        }
      }
    }

    def newDef(d : ASTTdef, e : Environment) : (String,BoundToFunc) = {
      val name = evaladv(d.name,e)._1.trim
      val params = d.dparams.dparams.map((x) => evaladv(x,e)._1.trim)
      val fnbound = new BoundToFunc(params,d.body,e)
      // we can add the binding only if the name string is non-empty
      if (name!="")
        e.addBinding(name,fnbound)
      // finally, return the binding, along with an empty string
      ("",fnbound)
    }

    def paramRef(i : ASTTvar, e : Environment) : (String,BoundToFunc) = {
      val param = i.name.trim
      val repl = e.getBindingRec(param)
      repl match {
        case null => {
          if (i.opt==null)
            (errorText("{{{"+param+"}}}"),null)
          else
            evaladv(i.opt,e)
        }
        case r : BoundToText => (r.text,null)
        case r : BoundToFunc => ("",r) // we can return a bound function too
      }
    }

    // Sometimes we have lists of things.  In the simpler eval we just concatenated
    // the strings to form the result.  Here, we still do that, but we also extract
    // the last template definition in the list as the Boundtofunc part of the returned pair.
    def accumulate(list: List[ASTNode], e: Environment) : (String,BoundToFunc) = {
      val evaluated = list.map((x) => evaladv(x,e))
      val s = evaluated.map((x) => x._1).mkString("")
      val f = evaluated.filter((x) => x._2 != null)
      if (f.length!=0)
        (s,f.last._2)
      else
        (s,null)
    }

    root match {
      case n : ASTProgram => accumulate(n.outer,e)
      case n : ASTInvoke => newInvoke(n,e)
      case n : ASTTvar => paramRef(n,e)
      case n : ASTTdef => newDef(n,e)
      case n : ASTText => (n.s,null)
      case n : ASTItext => accumulate(n.itext,e)
      case n : ASTDtext => accumulate(n.dtext,e)
      case _ => ("***",null)
    }
  }
  def eval2(root : ASTNode, e : Environment) : String = {
    evaladv(root,e)._1
  }
}

// -------------------------------------------------------------------------

// And finally a program to invoke the parser on a file or input string

object WML extends App {
  def help(): Unit = {
    println("scala WML.scala (-s string|filename)")
    System.exit(1)
  }

  if (args.length==0) {
    help()
  }

  val source = args match {
    case Array(_,"-s",s) => s
    case Array("-s",s) => s
    case Array(_,fn) => Source.fromFile(fn, "UTF-8").mkString
    case Array(fn) => Source.fromFile(fn, "UTF-8").mkString

    case Array("-s",s) => s
    case Array(fn) => Source.fromFile(fn, "UTF-8").mkString
  }

  val p = new WMLParser
  val result = p.parseAll(p.program,source);
  if (result.successful) {
    args match {
      case Array("-p",_*) => println(Evaluator.pprint(result.get))
      case _ => println(Evaluator.eval2(result.get,new Environment(null)))
    }
  } else {
    println("Parse failure: " + result);
  }
}
