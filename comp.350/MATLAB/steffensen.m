function root=steffensen(fname, x, xtol, ftol, nmax, display)
% Steffensen method.
% input:
%   fname   string that names the function f(x).
%   x       the initial point
%   xtol and ftol  termination tolerances
%   nmax    the maximum number of iteration
%   display = 1 if step-by-step display is desired,
%           = 0 otherwise
% output: root is the computed root of f(x)=0
n = 0;
fx = feval(fname,x);
if display
   disp('   n           x            f(x)')
   disp('-------------------------------------')
   disp(sprintf('%4d %23.15e %23.15e', n, x, fx))
end
if abs(fx) <= xtol
    root = x;
    return
end
for n = 1:nmax
    x_next = x + feval(fname, x);
    g = (feval(fname, x_next) - fx);
    div = fx^2/g;
    x = x - div;
    fx = feval(fname,x);
    if display
       disp(sprintf('%4d %23.15e %23.15e',n,x,fx))
    end
    if abs(div) <= xtol || abs(fx) <= ftol
        root = x;
        return
    end
end