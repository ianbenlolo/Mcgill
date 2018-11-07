// These tokens are trivial, made of the literal characters we want to recognize, and ensuring it is at
// the start of the string.
var TSTART = /^{{/;
var TEND = /^}}/;
var DSTART = /^{:/;
var DEND = /^:}/;
var PSTART = /^{{{/;
var PEND = /^}}}/;
// Pipe is a bit trickier, since "|" is a special character in RegExp's, so we need to escape it.
var PIPE = /^\|/;

// These rest rely on the same idea.  We match at least 1 character, consisting of a choice between
// something that could not be any of the disallowed tokens, or which is part of a disallowed
// token, but not followed by the rest of it.  For this we make use of the "x(?!y)" operator.

// For PNAME, we can recognize anything but "}", or "}" not followed by another "}", or "}}"
// not followed by another "}".
var PNAME = /^([^}|]|}(?!})|}}(?!}))+/;

// OUTERTEXT recognizes anything but "{", or "{" not followed by either of "{" or ":"
var OUTERTEXT = /^([^{]|{(?!({|:)))+/;

// INNERTEXT recognizes anything but "{" or "|", or "}", or "{" not followed by "{" or ":",
// or "}" not followed by another "}"
var INNERTEXT = /^([^{|}]|{(?!({|:))|}(?!}))+/;

// INNERDTEXT recognizes anything but "{" or "|", or ":", or "{" not followed by "{" or ":",
// or ":" not followed by "}"
var INNERDTEXT = /^([^{|:]|{(?!({|:))|:(?!}))+/;
