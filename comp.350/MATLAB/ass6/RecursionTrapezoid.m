function [result, y] = RecursionTrapezoid(a, b)
  %setting h to b-a, and x is the first step
h = b-a;
x = h/2*(f(0)+f(1));
count=2;
m = 0;
%if the difference between the actual erf(1) and x is
%greater than 10^-6 then recursively do the trapezoid computation
while abs(erf(1)-x) > 10^(-6)
    h = h/2;
    m = m+1;
    sum = 0;
    for i=1:2^(m-1)
        sum = sum + f(a+(2*i-1)*h);
        count = count + 1;
    end
    x = h*sum + x/2;
end
result = x;
y = count;
end