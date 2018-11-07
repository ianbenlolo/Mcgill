// Parsing the <outer> rule.
function parseOuter(s) {
    // As a base case, if we are fed the empty string just return null.
    if (s=="")
        return null;

    // Find out which of the 3 tokens we know about are at the start of the string.
    var t = scan(s,{OUTERTEXT:true,TSTART:true,DSTART:true});

    // Make up the object we will return; we modify fields below.
    var obj = {name:"outer",
               OUTERTEXT:null,
               templateinvocation:null,
               templatedef:null,
               next:null,
               // We'll keep track of the length of s we consumed in this and all
               // recursive calls here too.
               length:0};

    // And construct the returned object for each of the 3 cases.
    switch (t.token) {
    case 'OUTERTEXT':
        obj.OUTERTEXT = t.value;
        // Skip over consumed token.
        obj.length += t.value.length;
        s = s.substr(obj.length);
        break;
    case 'TSTART':
        obj.templateinvocation = parseTemplateInvocation(s);
        // Update how far we got in through the string.
        obj.length += obj.templateinvocation.length;
        s = s.substr(obj.templateinvocation.length);
        break;
    case 'DSTART':
        obj.templatedef = parseTemplateDef(s);
        // Update how far we got in through the string.
        obj.length += obj.templatedef.length;
        s = s.substr(obj.templatedef.length);
        break;
    }
    // We might have more outer pieces, so keep going.
    obj.next = parseOuter(s);
    // Update length to include everything we consumed.
    if (obj.next!==null)
        obj.length += obj.next.length;
    return obj;
}

// Parsing the <templateinvocation> rule. We assume the inital TSTART is at the front of the string.
function parseTemplateInvocation(s) {
    // Make up the object we will return; we modify fields below.
    var obj = {name:"templateinvocation",
               itext:null,
               targs:null,
               length:0};

    // First we need to skip over the initial token, which must be a TSTART.
    var t = scan(s,{TSTART:true});
    obj.length = t.value.length;
    // And skip over consumed token.
    s = s.substr(obj.length);

    // Next we find the name.  This is an itext, which is a list, and so might be empty.
    obj.itext = parseItext(s);
    if (obj.itext!=null) {
        obj.length += obj.itext.length;
        s = s.substr(obj.itext.length);
        // Strip WS.
        obj.itext = pruneWS(obj.itext,"INNERTEXT");
    }

    // Then parse through the argument list.  Again, this is a list, and it might be empty.
    obj.targs = parseTargs(s);
    if (obj.targs!=null) {
        obj.length += obj.targs.length;
        s = s.substr(obj.targs.length);
    }

    // Finally, we must end with a TEND, so this is guaranteed to exist.
    var t = scan(s,{TEND:true});
    obj.length += t.value.length;
    return obj;
}

// Remove leading and trailing whitespace from our lists.
// Not strictly necessary, as we have to prune it once we evaluate it again anyway,
// but since it was asked for in the assignment here it is.
// The field parameter should be INNERTEXT, or INNERDTEXT as necessary.
function pruneWS(list,field) {
    // Note that we assume our 
    function pruneLeading(list,field) {
        if (list!=null && list[field]!==null) {
            list[field] = list[field].replace(/^\s+/,'');
        }
        return list;
    }
    function pruneTrailing(inlist,field) {
        var list = inlist;
        while (list!=null && list.next!=null) 
            list = list.next;
        if (list!=null && list[field]!==null) {
            list[field] = list[field].replace(/\s+$/,'');
        }
        return inlist;
    }
    return pruneTrailing(pruneLeading(list,field),field);
}

// Parsing itext.  This returns a linked list of objects, possibly null.
function parseItext(s) {
    // An empty string could be a base case.  Strictly speaking, however, parsing <itext> 
    // should never actually terminate in anything other than a PIPE or a TEND, so this
    // is just being over-cautious.
    if (s=="")
        return null;

    // See which token is at the start of the string.
    var t = scan(s,{INNERTEXT:true,TSTART:true,DSTART:true,PSTART:true,PIPE:true,TEND:true});

    // If we scanned PIPE or TEND, then we are done, at a base case.
    if (t.token=="PIPE" || t.token=="TEND")
        return null;

    // Otherwise, we have a legitimate itext rule expansion, as INNERTEXT, an invoc, def, or param.
    var obj = {name:"itext",
               INNERTEXT:null,
               templateinvocation:null,
               templatedef:null,
               tparam:null,
               next:null,
               length:0};

    // And now build the object to be returned.
    switch (t.token) {
    case 'INNERTEXT':
        obj.INNERTEXT = t.value;
        // Skip over consumed token.
        obj.length += t.value.length;
        s = s.substr(obj.length);
        break;
    case 'TSTART':
        obj.templateinvocation = parseTemplateInvocation(s);
        // Update how far we got in through the string.
        obj.length += obj.templateinvocation.length;
        s = s.substr(obj.templateinvocation.length);
        break;
    case 'DSTART':
        obj.templatedef = parseTemplateDef(s);
        // Update how far we got in through the string.
        obj.length += obj.templatedef.length;
        s = s.substr(obj.templatedef.length);
        break;
    case 'PSTART':
        obj.tparam = parseTParam(s);
        // Update how far we got in through the string.
        obj.length += obj.tparam.length;
        s = s.substr(obj.tparam.length);
        break;
    }

    // We might have more pieces to the itext list, so keep going.
    obj.next = parseItext(s);
    // Update length consumed to include the remaining pieces too
    if (obj.next!==null)
        obj.length += obj.next.length;
    return obj;
}

// Parsing targs.  This is another list.
function parseTargs(s) {
    // To start with we should see a PIPE or a TEND.  If we see TEND, then
    // we are done with our list.
    var t = scan(s,{PIPE:true,TEND:true});
    if (t.token=='TEND') 
        return null;

    // Ok, we saw a PIPE, so we know we have an argument (and maybe more).

    var obj = {name:"targs",
               itext:null,
               next:null,
               length:t.value.length};

    // Skip over the PIPE.
    s = s.substr(obj.length);

    // Parse the ensuing itext.
    obj.itext = parseItext(s);
    if (obj.itext!=null) {
        obj.length += obj.itext.length;
        s = s.substr(obj.itext.length);
        obj.itext = pruneWS(obj.itext,"INNERTEXT");
    }

    // There might be more arguments, so keep parsing recursively.
    obj.next = parseTargs(s);
    if (obj.next!=null)
        obj.length += obj.next.length;
    return obj;
}

// Parsing <templatedef>.  Very much like parsing an invocation, we get here once we've
// already recognized the DSTART, so we know it starts with one.
function parseTemplateDef(s) {
    var obj = {name:"templatedef",
               // It's all one big list of dtext, but it's a bit easier if we at least split
               // off the name from the rest of it.
               dtext:null,
               dparams:null,
               length:0};

    // First we need to skip over the initial token, which must be a DSTART.
    var t = scan(s,{DSTART:true});
    obj.length = t.value.length;
    // And skip over consumed token.
    s = s.substr(obj.length);

    // Next we find the template name.  This is a dtext.
    obj.dtext = parseDtext(s);
    if (obj.dtext!=null) {
        obj.length += obj.dtext.length;
        s = s.substr(obj.dtext.length);
        // Strip WS.
        obj.dtext = pruneWS(obj.dtext,"INNERDTEXT");
    }

    // Then the parameter list.
    obj.dparams = parseDparams(s);
    // The dparams list cannot be null, as we always have a body.
    obj.length += obj.dparams.length;
    s = s.substr(obj.dparams.length);

    // Clean off any leading/trailing ws from the args, but not the body.
    var d = obj.dparams;
    while(d.next!=null) {
        d.dtext = pruneWS(d.dtext,"INNERDTEXT");
        d = d.next;
    }

    // Finally, we must end with a DEND, so this is guaranteed to exist.
    var t = scan(s,{DEND:true});
    obj.length += t.value.length;
    return obj;
}

// Parsing dtext.  This is quite similar to parseItext, just terminating
// in a DEND instead of TEND, and including INNERDTEXT instead of INNERTEXT.
function parseDtext(s) {
    // Trivial base case check.
    if (s=="")
        return null;

    // See which token is at the start of the string.
    var t = scan(s,{INNERDTEXT:true,TSTART:true,DSTART:true,PSTART:true,PIPE:true,DEND:true});

    // If we scanned PIPE or DEND, then we are done, at a base case.
    if (t.token=="PIPE" || t.token=="DEND")
        return null;

    // Otherwise, we have a legitimate dtext rule expansion, as INNERDTEXT, an invoc, def, or param.
    var obj = {name:"dtext",
               INNERDTEXT:null,
               templateinvocation:null,
               templatedef:null,
               tparam:null,
               next:null,
               length:0};

    // And now build the object to be returned.
    switch (t.token) {
    case 'INNERDTEXT':
        obj.INNERDTEXT = t.value;
        obj.length += t.value.length;
        // Skip over consumed token.
        s = s.substr(obj.length);
        break;
    case 'TSTART':
        obj.templateinvocation = parseTemplateInvocation(s);
        // Update how far we got in through the string.
        obj.length += obj.templateinvocation.length;
        s = s.substr(obj.templateinvocation.length);
        break;
    case 'DSTART':
        obj.templatedef = parseTemplateDef(s);
        // Update how far we got in through the string.
        obj.length += obj.templatedef.length;
        s = s.substr(obj.templatedef.length);
        break;
    case 'PSTART':
        obj.tparam = parseTParam(s);
        // Update how far we got in through the string.
        obj.length += obj.tparam.length;
        s = s.substr(obj.tparam.length);
        break;
    }

    // We might have more pieces to the dtext list, so keep going.
    obj.next = parseDtext(s);
    // Update length consumed to include the remaining pieces too
    if (obj.next!==null)
        obj.length += obj.next.length;
    return obj;
}

// Parsing dparams.  This is another list, of parameters, and the body.
function parseDparams(s) {
    // To start with we should see a PIPE or a DEND.  If we see DEND, then
    // we are done with our list.
    var t = scan(s,{PIPE:true,DEND:true});
    if (t.token=='DEND') 
        return null;

    // Ok, we saw a PIPE, so we know we have an parameter (or body).
    var obj = {name:"dparams",
               dtext:null,
               next:null,
               length:t.value.length};

    // Skip over the PIPE.
    s = s.substr(obj.length);

    // Parse the ensuing dtext.
    obj.dtext = parseDtext(s);
    if (obj.dtext!=null) {
        obj.length += obj.dtext.length;
        s = s.substr(obj.dtext.length);
    }

    // There might be more, so keep parsing recursively.
    obj.next = parseDparams(s);
    if (obj.next!=null)
        obj.length += obj.next.length;
    return obj;
}

// Parsing a <tparam> structure.
function parseTParam(s) {
    // We get here having already seen the PSTART, so 
    // we just need to skip over that and get the name and the PEND.

    var obj = {name:"tparam",
               pname:null,
               length:0};

    // First we need to skip over the initial token, which must be a PSTART.
    var t = scan(s,{PSTART:true});
    obj.length = t.value.length;
    // And skip over consumed token.
    s = s.substr(obj.length);

    // Now scan the parameter name.
    t = scan(s,{PNAME:true});
    
    obj.pname = t.value.trim();
    obj.length += t.value.length;
    s = s.substr(t.value.length);

    // And the PEND.
    t = scan(s,{PEND:true});
    obj.length += t.value.length;
    return obj;
}
