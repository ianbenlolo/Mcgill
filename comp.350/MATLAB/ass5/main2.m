function main=main2()

%printing coefficients
%change 0 to 1 for chebyshev
n=0;
disp('Coefficients of p(x), x0...x6 =');
disp(Newton(n));
disp('Coefficients of S(x), x0...x6 = ')
disp(calcZ(n))
disp('Least  square coefficients g(x) (a,b,c respectively)= ')
disp(leastSquares(n));

%setting the requierd nodes
for i=0:12
    x(i+1)=-1+i/6;
end
x=transpose(x);

for i=1:length(x)
    fi(i)=f(x(i));
    new(i)=newtonEval(x(i));
    least(i)=leastSquaresEval(x(i));
end
fi=transpose(fi);
%%%

for i=1:13
    cubi(i)=cubicSpline(x(i));
end
cubi=transpose(cubi);
disp('f(x)= ')
disp(transpose(fi))
for i=1:13
    fmp(i)=fi(i)-new(i);
    fmc(i)=fi(i)-cubi(i);
    fml(i)=fi(i)-least(i);
end
disp('f(x)-p(x)= ')
disp(fmp)
disp('f(x)-S(x) = ')
disp(fmc)
disp('f(x)-g(x)= ')
disp(fml)

