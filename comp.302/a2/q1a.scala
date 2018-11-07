//Ian Benlolo 260744397
// q1a.scala
//Assignment 2 comp 302 -Programming languages and Paradigms
// 28/02/18

val Tstart = "{{"
val Tend = "}}"
val Vstart = "{{{"
val Vend = "}}}"
val Dstart = "{''"
val Dend = "'}"
val Pipe = "|"
val Pipes = "||"

val Outertext = "^([^\\{]|(\\{(?!(\\{)|('))))+".r
val Inneritext = "^([^\\{\\|\\}]|(\\{(?!(\\{)|(\\{\\{)|(\\')))|\\}(?!(\\})))+".r
val Innerdtext = "^([^\\{\\|\\']|(\\{(?!(\\{)|(\\{\\{)|(\\')))|\\'(?!(\\})))+".r
val Bodytext = "^([^\\{\\|\\']|(\\{(?!(\\{)|(\\')|(\\{\\{)))|\\'(?!(\\})))+".r
val Vname = "^([^\\|\\'\\}]|\\}(?!(\\}\\})))+".r
