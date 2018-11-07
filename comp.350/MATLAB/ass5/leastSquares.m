function c=leastSquares(coord)
%coord=0 for 7 equally spaced nodes, 1 for chebyshev nodes
n=7;
if coord==0
    x=linspace(-1,1,7);
elseif coord==1 %for chebyshev
    for i=0:6
        x(i+1)=cos((2*i+1)*pi/(14));
    end
end
for i=1:7
    y(i)=f(x(i));
end
a=zeros(n,3);
for i=1:n
    a(i,1)=x(i)^0;
    a(i,2)=(x(i))^2;
    a(i,3)=(x(i))^4;
end
c=a\transpose(y);