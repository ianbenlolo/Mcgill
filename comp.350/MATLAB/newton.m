function root=newton(fname, fdname, x, xtol, ftol, nmax, display)
% Newton method.
% input:
%   fname   string that names the function f(x).
% fdname the string that names the derivative function df(x)/dx
%   x       the initial point
%   xtol and ftol  termination tolerances
%   nmax    the maximum number of iteration
%   display = 1 if step-by-step display is desired,
%           = 0 otherwise
% output: root is the computed root of f(x)=0
n=0;
fx = feval(fname, x);
if display
       disp('   n            x                    f(x)')
       disp('------------------------------------')
       disp(sprintf('%4d %23.15e %23.15e\n', n, x, fx))
end
for n=1:nmax
    fdx=feval(fdname,x);
    d=fx/fdx;
    x=x-d;
    fx=feval(fname,x);
    if display,
        disp(sprintf('%4d %23.15e %23.15e', n,x,fx))
    end
    if abs(d)<=xtol | abs(fx)<=ftol
        root=x;
        return
    end
end