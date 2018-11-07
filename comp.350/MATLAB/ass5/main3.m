function m=main3()
for i=0:12
    x(i+1)=-1+i/6;
end
x=transpose(x);
for i=1:length(x)
    least(i)=leastSquaresEval(x(i));
end
for i=0:6
    x(i+1)=cos((2*i+1)*pi/(14));
end
for i=1:7
    y(i)=f(x(i));
end
y