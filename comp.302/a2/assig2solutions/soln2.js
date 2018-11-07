// Returns the token located at the beginning of s, where the set of allowed tokens
// is given by an object tokenset, as per the assignment format.
// Syntactically correct input is assumed, and the tokenset is assumed appropriate
// for the input too, so we do not need to check for errors of any form.
function scan(s,tokenset) {
    // Inside here we just need to check for each regexp we defined in q1.
    // Tokens are disjoint in all valid cases, except for TSTART and PSTART,
    // which we resolve by always checking for PSTART first.

    // just for debugging
    // var ss = "{ ";
    // for (var t in tokenset) {
    //     ss += t + ": "+tokenset[t]+", ";
    // }
    // addDebugText("Token set: "+ss+"}\n");

    // To go over all tokens we create an array of objects mapping names to the 
    // corresponding regexp variables we created in q1.  We use an array
    // so we can check them in a specific order.
    var tokens = [
        { name:"PSTART", regexp:PSTART },
        { name:"PEND", regexp:PEND },
        { name:"TSTART", regexp:TSTART }, 
        { name:"TEND", regexp:TEND } ,
        { name:"DSTART", regexp:DSTART },
        { name:"DEND", regexp:DEND },
        { name:"PIPE", regexp:PIPE },
        { name:"PNAME", regexp:PNAME },
        { name:"OUTERTEXT", regexp:OUTERTEXT },
        { name:"INNERTEXT", regexp:INNERTEXT },
        { name:"INNERDTEXT", regexp:INNERDTEXT } ];

    // Now, iterative through our tokens array, and see what we find
    for (var i=0; i<tokens.length; i++) {
        var m;
        if (tokenset[tokens[i].name] && (m = s.match(tokens[i].regexp))) {
            return { token:tokens[i].name, value:m[0] };
        }
    }
    throw "Hey, there aren't supposed to be syntactic errors, but I encountered \""+s+"\"";
}

