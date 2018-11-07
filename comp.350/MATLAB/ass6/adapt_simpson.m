function soln = adapt_simpson(a,b,ep,lvl, lvl_max, count)
h = b-a;
c = (a+b)/2;
i1=h*(f(a)+4*f(c)+f(b))/6;
count = count + 3;
lvl = lvl + 1;
d=(a+c)/2;
e=(c+b)/2;
i2 = h*(f(a)+4*f(d)+2*f(c)+4*f(e)+f(b))/12;
count = count + 5;
if lvl >= lvl_max
    numbI = i2;
    soln = [numbI, count];
else
    if abs(i2-i1) <= 15*ep
        numbI = i2+(1/15)*(i2-i1);
        soln = [numbI, count];
    else
       soln = adapt_simpson(a,c,ep/2,lvl, lvl_max, count) + adapt_simpson(c,b,ep/2,lvl, lvl_max, count);
    end
end
end