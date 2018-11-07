function y = newtonEval(x)
%coord=0for the 7 equally spaced, 1 for chebyshev
nodes= 0;
a = Newton(nodes);
n = length(a);

if nodes==0
    coord=linspace(-1,1,7);
elseif nodes==1 %for chebyshev
    for i=0:6
        coord(i+1)=cos((2*i+1)*pi/(2*6+2));
    end
end

y = a(n);

xi = coord(1,:);

for i=n-1: -1:1
    y = a(i) + y.*(x-xi(i));
end