// Pretty printer for our AST.  Returns a string representation of the input.
// This function is really simple.  We just iterate through the AST linked-list
// pieces, switching to find out what the node type is, printing its components
// directly or via recursive calls.
function printAST(a) {
    var sout = ""; // We concat all output to this string and return it.

    // Iterate through the linked list.
    while (a!=null) {

        switch(a.name) {

            case "outer":
            if (a.OUTERTEXT!=null)
                sout += a.OUTERTEXT;
            else if (a.templateinvocation!=null)
                sout += printAST(a.templateinvocation);
            else if (a.templatedef!=null)
                sout += printAST(a.templatedef);
            break;

            case "templateinvocation":
            sout += "{{";
            if (a.itext!=null) {
                // An invocation name does not have leading/trailing WS.
                var tname = printAST(a.itext);
                // If the name starts with a "{" (because there's a nested invoke, param,
                // or def), then introduce a space to avoid accidentally converting 
                // the TSTART "{{" into a PSTART "{{{".
                if (tname.length>0 && tname.charAt(0)=='{')
                    sout += " ";
                sout += tname;
            }
            // Append args, if any.
            if (a.targs!=null)
                sout += printAST(a.targs);
            sout += "}}";
            break;

            case "itext":
            if (a.INNERTEXT!=null)
                sout += a.INNERTEXT;
            else if (a.templateinvocation!=null)
                sout += printAST(a.templateinvocation);
            else if (a.templatedef!=null)
                sout += printAST(a.templatedef);
            else if (a.tparam!=null)
                sout += printAST(a.tparam);
            break;

            case "targs":
            // Args are prepended by a PIPE.
            sout += "|";
            if (a.itext!=null)
                sout += printAST(a.itext);
            break;

            case "templatedef":
            sout += "{:";
            // Print the definition name, without leading/trailing WS.  
            // Check before trimming though, as it could be null.
            if (a.dtext!=null)
                sout += printAST(a.dtext);
            sout += printAST(a.dparams);
            sout += ":}";
            break;

            case "dtext":
            if (a.INNERDTEXT!=null)
                sout += a.INNERDTEXT;
            else if (a.templateinvocation!=null)
                sout += printAST(a.templateinvocation);
            else if (a.templatedef!=null)
                sout += printAST(a.templatedef);
            else if (a.tparam!=null)
                sout += printAST(a.tparam);
            break;

            case "dparams":
            // Like targs, individual dparams are separated by a PIPE.
            sout += "|";
            if (a.dtext!=null) {
                sout += printAST(a.dtext);
            }
            break;

            case "tparam":
            // A parameter name itself should not have leading/trailing WS.
            sout += "{{{" + a.pname.trim() + "}}}";
            break;

            default: 
            // We shouldn't need this, but just in case.
            sout += "ERROR("+a.name+")";
            break;
        }
        a = a.next;
    }
    return sout;
}
