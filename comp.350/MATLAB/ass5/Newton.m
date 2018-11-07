function a=Newton(nodes)
n=7;
if nodes==0
    xs=linspace(-1,1,7);
elseif nodes==1 %for chebyshev
    for i=0:6
        xs(i+1)=cos((2*i+1)*pi/(2*6+2));
    end
end
for i=1:n
    y(i)=f(xs(i));
end

for k=1:n-1
    a(k)=y(k);
    for i=k+1:n
        y(i)=(y(i)-y(k))/(xs(i)-xs(k));
    end
end
a(n)=y(n);