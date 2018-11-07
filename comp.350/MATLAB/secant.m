function root=secant(fname,x,x0 , xtol,ftol,nmax,display)
% Secant method.
% input:
%   fname   string that names the function f(x).
%   x0,x       two initial point
%   xtol, ftol termination tolerances
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
    fx0=feval(fname, x0);
    div = (x - x0)/(fx - fx0) * fx;
    x0 = x;
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